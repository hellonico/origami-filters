package origami.filters.detect;

import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;
import origami.Detect;
import origami.Filter;
import origami.utils.Utils;

import java.util.List;

import static org.opencv.imgproc.Imgproc.*;
import static origami.utils.Utils.Scalar_String;
import static origami.utils.Utils.String_Scalar;

public class Haar implements Filter, Detect {

    private CascadeClassifier classifier;
    Scalar color = new Scalar(255, 255, 255);
    String text = "Gatos";
    String type;
    double scaleFactor = 1.1;
    int minNeighbors = 2;
    Size minSize = new Size(300, 300);
    Size maxSize = new Size(500, 500);

    public String getColor() {
        return Scalar_String(this.color);
    }

    public void setColor(String color) {
        this.color = String_Scalar(color);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Haar() {
        this.setType("haar.frontalcatface");
    }

    public void setType(String type) {
        this.type = type;
        classifier = Utils.loadCascadeClassifier(type);
    }

    public String getType() {
        return this.type;
    }

    public Mat apply(Mat input) {
        List<Rect> faces = detectROI(input);

        for (Rect rect : faces) {
            putText(input, text, new Point(rect.x, rect.y - 5), 3, 3, color, 5);
            rectangle(input, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    color, 5);
        }
        return input;
    }

    public List<Rect> detectROI(Mat input) {
        MatOfRect faces = new MatOfRect();
        classifier.detectMultiScale(input, faces, scaleFactor, minNeighbors, -1, minSize, maxSize);
        return faces.toList();
    }

}

