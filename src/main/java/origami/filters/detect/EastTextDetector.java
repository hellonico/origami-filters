package origami.filters.detect;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * https://stackoverflow.com/questions/53402064/opencv-east-text-detector-implementation-in-java
 * https://bitbucket.org/tomhoag/opencv-text-detection/src/dbe0952752535c4d7e5cca1289a3e2118cb878dd/opencv_text_detection/frozen_east_text_detection.pb?at=master
 * https://www.pyimagesearch.com/2018/08/20/OpenCV-text-detection-east-text-detector/
 */

public class EastTextDetector implements Filter {
    Net net;

    // forced by the network
    Size siz = new Size(320, 320);
    int W = (int)(siz.width / 4); // width of the output geometry  / score maps
    int H = (int)(siz.height / 4); // height of those. the geometry has 4, vertically stacked maps, the score one 1


    public EastTextDetector() {
        net = (Net) origami.Dnn.readNetFromSpec("networks.tensorflow:east-text-detection:1.0.0").get(0);
    }

    public Mat apply(Mat frame) {
        float scoreThresh = 0.5f;
        float nmsThresh = 0.4f;
        // Model from https://github.com/argman/EAST
        // You can find it here : https://github.com/opencv/opencv_extra/blob/master/testdata/dnn/download_models.py#L309

        // input image
        // Mat frame = Imgcodecs.imread("data/mdl/m2.jpg");
        // Core.bitwise_not(frame,frame);
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);

        Mat blob = Dnn.blobFromImage(frame, 1.0,siz, new Scalar(123.68, 116.78, 103.94), true, false);
        net.setInput(blob);
        List<Mat> outs = new ArrayList<>(2);
        List<String> outNames = new ArrayList<>();
        outNames.add("feature_fusion/Conv_7/Sigmoid");
        outNames.add("feature_fusion/concat_3");
        net.forward(outs, outNames);

        // Decode predicted bounding boxes.
        Mat scores = outs.get(0).reshape(1, H);
        // My lord and savior : http://answers.opencv.org/question/175676/javaandroid-access-4-dim-mat-planes/
        Mat geometry = outs.get(1).reshape(1, 5 * H); // don't hardcode it !
        List<Float> confidencesList = new ArrayList<>();
        List<RotatedRect> boxesList = decode(scores, geometry, confidencesList, scoreThresh);

        // Apply non-maximum suppression procedure.
        MatOfFloat confidences = new MatOfFloat(Converters.vector_float_to_Mat(confidencesList));
        RotatedRect[] boxesArray = boxesList.toArray(new RotatedRect[0]);
        MatOfRotatedRect boxes = new MatOfRotatedRect(boxesArray);
        MatOfInt indices = new MatOfInt();
        Dnn.NMSBoxesRotated(boxes, confidences, scoreThresh, nmsThresh, indices);

        return renderDetection(frame, boxesArray, indices);
    }

    private Mat renderDetection(Mat frame, RotatedRect[] boxesArray, MatOfInt indices) {
        // Render detections
        Point ratio = new Point((float) frame.cols()/ siz.width, (float) frame.rows()/ siz.height);
        int[] indexes = indices.toArray();
        for (int index : indexes) {
            RotatedRect rot = boxesArray[index];
            Point[] vertices = new Point[4];
            rot.points(vertices);
            for (int j = 0; j < 4; ++j) {
                vertices[j].x *= ratio.x;
                vertices[j].y *= ratio.y;
            }
            for (int j = 0; j < 4; ++j) {
                Imgproc.line(frame, vertices[j], vertices[(j + 1) % 4], new Scalar(0, 0, 255), 1);
            }
        }
        return frame;
    }

    private static List<RotatedRect> decode(Mat scores, Mat geometry, List<Float> confidences, float scoreThresh) {
        // size of 1 geometry plane
        int W = geometry.cols();
        int H = geometry.rows() / 5;

        List<RotatedRect> detections = new ArrayList<>();
        for (int y = 0; y < H; ++y) {
            Mat scoresData = scores.row(y);
            Mat x0Data = geometry.submat(0, H, 0, W).row(y);
            Mat x1Data = geometry.submat(H, 2 * H, 0, W).row(y);
            Mat x2Data = geometry.submat(2 * H, 3 * H, 0, W).row(y);
            Mat x3Data = geometry.submat(3 * H, 4 * H, 0, W).row(y);
            Mat anglesData = geometry.submat(4 * H, 5 * H, 0, W).row(y);

            for (int x = 0; x < W; ++x) {
                double score = scoresData.get(0, x)[0];
                if (score >= scoreThresh) {
                    double offsetX = x * 4.0;
                    double offsetY = y * 4.0;
                    double angle = anglesData.get(0, x)[0];
                    double cosA = Math.cos(angle);
                    double sinA = Math.sin(angle);
                    double x0 = x0Data.get(0, x)[0];
                    double x1 = x1Data.get(0, x)[0];
                    double x2 = x2Data.get(0, x)[0];
                    double x3 = x3Data.get(0, x)[0];
                    double h = x0 + x2;
                    double w = x1 + x3;
                    Point offset = new Point(offsetX + cosA * x1 + sinA * x2, offsetY - sinA * x1 + cosA * x2);
                    Point p1 = new Point(-1 * sinA * h + offset.x, -1 * cosA * h + offset.y);
                    Point p3 = new Point(-1 * cosA * w + offset.x,      sinA * w + offset.y); // original trouble here !
                    RotatedRect r = new RotatedRect(new Point(0.5 * (p1.x + p3.x), 0.5 * (p1.y + p3.y)), new Size(w, h), -1 * angle * 180 / Math.PI);
                    detections.add(r);
                    confidences.add((float) score);
                }
            }
        }
        return detections;
    }
}