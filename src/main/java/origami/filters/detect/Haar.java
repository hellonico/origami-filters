package origami.filters.detect;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import origami.Filter;

class Haar implements Filter {

    private CascadeClassifier classifier;
    Scalar white = new Scalar(255, 255, 255);
    String text = "Gatos";

    public Haar(String path) {
        classifier = new CascadeClassifier(path);
    }

    public Mat apply(Mat input) {
        MatOfRect faces = new MatOfRect();
        classifier.detectMultiScale(input, faces, 1.1, 2, -1, new Size(300, 300), new Size(500, 500));
        for (Rect rect : faces.toArray()) {
            Imgproc.putText(input, text, new Point(rect.x, rect.y - 5), 3, 3, white, 5);
            Imgproc.rectangle(input, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    white, 5);
        }
        return input;
    }
}

