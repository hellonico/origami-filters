package origami.filters.brandnew;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.colors.HTML;

import java.util.ArrayList;
import java.util.List;

public class FindContours implements Filter {
    int maxLevel = 2;

    int thickness = 3;

    Scalar color = HTML.toScalar("#2151515");

    double threshold = 100;

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public String getColor() {
        return HTML.toHTML(color);
    }

    public void setColor(String color) {
        this.color = HTML.toScalar(color);
    }

    @Override
    public Mat apply(Mat src) {
        Mat res = new Mat(src.rows(), src.cols(), src.type());
        Mat gray = new Mat(src.rows(), src.cols(), src.type());
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Mat binary = new Mat(src.rows(), src.cols(), src.type(), new Scalar(0));
        Imgproc.threshold(gray, binary, threshold, 255, Imgproc.THRESH_BINARY_INV);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        Imgproc.drawContours(res, contours, -1, color, thickness, Imgproc.LINE_AA, hierarchy, 2, new Point());

        return res;
    }
}
