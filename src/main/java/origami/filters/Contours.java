package origami.filters;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.colors.HTML;
import origami.colors.RGB;

import java.util.ArrayList;
import java.util.List;

public class Contours implements Filter {
    private int threshold = 100;
    Scalar color = RGB.cyan_2;
    int thickness = 2;
    int linetype = 8;
    private Point offset = new Point();

    public String getOffset() {
        return Utils.Point_String(offset);
    }

    public void setOffset(String offset) {
        this.offset = Utils.String_Point(offset);
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String getColor() {
        return HTML.toHTML(color);
    }

    public void setColor(String color) {
        this.color = HTML.toScalar(color);
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public int getLinetype() {
        return linetype;
    }

    public void setLinetype(int linetype) {
        this.linetype = linetype;
    }

    public Mat apply(Mat srcImage) {
        Mat cannyOutput = new Mat();
        Mat srcGray = new Mat();
        Imgproc.cvtColor(srcImage, srcGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(srcGray, cannyOutput, threshold, Math.max(threshold * 2,255));
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(drawing, contours, i, color, thickness, linetype, hierarchy, 0, offset);
        }
        return drawing;
    }

}