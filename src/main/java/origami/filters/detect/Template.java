package origami.filters.detect;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class Template implements Filter {
    Mat template;

    public Template(String path) {
        this.template = Imgcodecs.imread(path);

    }

    public void drawOver(Mat in, Point matchLoc){
        Imgproc.rectangle(in, matchLoc, new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                new Scalar(255, 255, 255), 3);
    }

    @Override
    public Mat apply(Mat in) {

        Mat outputImage = new Mat();
        Imgproc.matchTemplate(in, template, outputImage, Imgproc.TM_CCOEFF);

        Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
        Point matchLoc = mmr.maxLoc;
        drawOver(in, matchLoc);

        return in;

    }

}