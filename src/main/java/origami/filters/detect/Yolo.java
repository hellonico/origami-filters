package origami.filters.detect;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Yolo implements Filter {
    final static Size sz = new Size(416, 416);
    List<String> outBlobNames;
    Net net;
    List<String> layers;
    List<String> labels;
    float scoreThreshold = 0.1f;
    float nmsThreshold = 0.1f;

    List<String> getOutputsNames(Net net) {
        List<String> layersNames = net.getLayerNames();
        return net.getUnconnectedOutLayers().toList().stream().map(i -> i - 1).map(layersNames::get)
                .collect(Collectors.toList());
    }

    public Yolo(String spec) {
        List _net = origami.Dnn.readNetFromSpec(spec);
        net = (Net) _net.get(0);
        labels = (List<String>) _net.get(2);
        layers = getOutputsNames(net);
    }

    public Yolo thresholds(float score, float nms) {
        this.scoreThreshold = score;
        this.nmsThreshold = nms;
        return this;
    }

    @Override
    public Mat apply(Mat in) {
        findShapes(in);
        return in;
    }

    final int IN_WIDTH = 416;
    final int IN_HEIGHT = 416;
    final double IN_SCALE_FACTOR = 0.00392157;
    final int MAX_RESULTS = 20;
    final boolean SWAP_RGB = true;
//    final String LABEL_FILE = "yolov3/coco.names";

    void findShapes(Mat frame) {

        Mat blob = Dnn.blobFromImage(frame, IN_SCALE_FACTOR, new Size(IN_WIDTH, IN_HEIGHT), new Scalar(0, 0, 0),
                SWAP_RGB);
        net.setInput(blob);

        List<Mat> outputs = new ArrayList<>();
        for (int i = 0; i < layers.size(); i++)
            outputs.add(new Mat());

        net.forward(outputs, layers);
        postprocess(frame, outputs);
    }

    private void postprocess(Mat frame, List<Mat> outs) {

        List<Rect2d> tmpLocations = new ArrayList<>();
        List<Integer> tmpClasses = new ArrayList<>();
        List<Float> tmpConfidences = new ArrayList<>();

        int w = frame.width();
        int h = frame.height();

        for (Mat out : outs) {

            final float[] data = new float[(int) out.total()];
            out.get(0, 0, data);

            int k = 0;
            for (int j = 0; j < out.height(); j++) {

                Mat scores = out.row(j).colRange(5, out.width());
                Core.MinMaxLocResult result = Core.minMaxLoc(scores);
                if (result.maxVal > 0) {
                    float center_x = data[k + 0] * w;
                    float center_y = data[k + 1] * h;
                    float width = data[k + 2] * w;
                    float height = data[k + 3] * h;
                    float left = center_x - width / 2;
                    float top = center_y - height / 2;

                    tmpClasses.add((int) result.maxLoc.x);
                    tmpConfidences.add((float) result.maxVal);
                    tmpLocations.add(new Rect2d((int) left, (int) top, (int) width, (int) height));

                }
                k += out.width();
            }
        }

        annotateFrame(frame, tmpLocations, tmpClasses, tmpConfidences);
    }

    private void annotateFrame(Mat frame, List<Rect2d> tmpLocations, List<Integer> tmpClasses,
                               List<Float> tmpConfidences) {

        MatOfRect2d locMat = new MatOfRect2d();
        MatOfFloat confidenceMat = new MatOfFloat();
        MatOfInt indexMat = new MatOfInt();

        locMat.fromList(tmpLocations);
        confidenceMat.fromList(tmpConfidences);

        Dnn.NMSBoxes(locMat, confidenceMat, 0.1f, 0.1f, indexMat);

        try {
            processResults(frame, tmpLocations, tmpClasses, indexMat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processResults(Mat frame, List<Rect2d> tmpLocations, List<Integer> tmpClasses, MatOfInt indexMat) {
//        List<Integer> ints = indexMat.total() > MAX_RESULTS ? indexMat.toList().subList(0, MAX_RESULTS) : indexMat.toList();
//         List<List> results = ints.stream().map(i-> {
//             int idx = (int) indexMat.get(i, 0)[0];
//             int labelId = tmpClasses.get(idx);
//             Rect box = tmpLocations.get(idx);
//             String label = labels.get(labelId);
//             return Arrays.asList(box,label);
//         }).collect(Collectors.toList());

//        for (int i = 0; i < indexMat.total() && i < MAX_RESULTS; ++i) {
//            int idx = (int) indexMat.get(i, 0)[0];
//            int labelId = tmpClasses.get(idx);
//            Rect box = tmpLocations.get(idx);
//            String label = labels.get(labelId);
//            annotateOne(frame, box, label);
//        }

        List<List> results = new ArrayList();
        for (int i = 0; i < indexMat.total() && i < MAX_RESULTS; ++i) {
            int idx = (int) indexMat.get(i, 0)[0];
            int labelId = tmpClasses.get(idx);
            Rect2d box = tmpLocations.get(idx);
            String label = labels.get(labelId);
            results.add(Arrays.asList(box, label));
        }

        annotateAll(frame, results);
    }

    public void annotateAll(Mat frame, List<List> results) {
        for (List result : results) {
            annotateOne(frame, (Rect) result.get(0), (String) result.get(1));
        }
    }

    public void annotateOne(Mat frame, Rect box, String label) {
        Imgproc.rectangle(frame, box, new Scalar(0, 0, 0), 2);
        Imgproc.putText(frame, label, new Point(box.x, box.y), Imgproc.FONT_HERSHEY_PLAIN, 4.0, new Scalar(0, 0, 0), 3);
    }

}