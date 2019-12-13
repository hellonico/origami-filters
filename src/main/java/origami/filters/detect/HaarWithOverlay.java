package origami.filters.detect;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

public class HaarWithOverlay  implements Filter {

    CascadeClassifier classifier;
    Mat mask;
    Scalar white = new Scalar(255, 255, 255);
    double adjust = 0.2;
//    String overlay = "masquerade_mask.png";

    public HaarWithOverlay(String path, String overlay) {
        classifier = new CascadeClassifier(path);
        mask = Imgcodecs.imread(overlay, Imgcodecs.IMREAD_UNCHANGED);
    }

    void drawTransparency(Mat frame, Mat transp, int xPos, int yPos) {
        List<Mat> layers = new ArrayList<Mat>();
        Core.split(transp, layers);
        Mat mask = layers.remove(3);
        Core.merge(layers, transp);
        Mat submat = frame.submat(yPos, yPos + transp.rows(), xPos, xPos + transp.cols());
        transp.copyTo(submat, mask);
    }

    public Mat apply(Mat input) {
        MatOfRect faces = new MatOfRect();
        classifier.detectMultiScale(input, faces);
        Mat maskResized = new Mat();
        for (Rect rect : faces.toArray()) {
            Imgproc.resize(mask, maskResized, new Size(rect.width, rect.height));
            int adjusty = (int) (rect.y - rect.width * adjust);
            try {
                drawTransparency(input, maskResized, rect.x, adjusty);
            } catch (Exception e) {
//                e.printStackTrace();
                //may get out the picture
            }
        }
        return input;
    }
}