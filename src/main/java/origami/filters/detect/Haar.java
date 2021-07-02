package origami.filters.detect;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import origami.Dnn;
import origami.Fetcher;
import origami.Filter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Haar implements Filter {

    private CascadeClassifier classifier;
    Scalar white = new Scalar(255, 255, 255);
    String text = "Gatos";
    private String type;

    public Scalar getWhite() {
        return white;
    }

    public void setWhite(Scalar white) {
        this.white = white;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Haar() {

    }

    public void setType(String type) {
        this.type = type;
        String folder = Fetcher.fetchFromSpec("origami.artefacts:cascades:1.0.0");
        File f = Objects.requireNonNull(new File(folder).listFiles())[0];
        File[] files = f.listFiles();
        File eye = Arrays.stream(files).filter(_f->_f.getName().contains(type)).findFirst().get();
        this.setFile(eye.getAbsolutePath());
    }
    public String getType() {
        return this.type;
    }

    public void setFile(String file) {
        classifier = new CascadeClassifier(file);
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

