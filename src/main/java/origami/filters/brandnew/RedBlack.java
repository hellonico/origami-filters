package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.colors.HTML;

import java.util.ArrayList;
import java.util.List;

public class RedBlack implements Filter {
    String color0 = "black";

    public String getColor0() {
        return color0;
    }

    public void setColor0(String color0) {
        this.color0 = color0;
    }

    String color1 = "red";
    String color2 = "grey";

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String contourColor = "white";

    public String getContourColor() {
        return contourColor;
    }

    public void setContourColor(String contourColor) {
        this.contourColor = contourColor;
    }

    @Override
    public Mat apply(Mat image) {

        Mat reducedColors = new Mat();
        Imgproc.cvtColor(image, reducedColors, Imgproc.COLOR_BGR2HSV);

        // Define the color ranges
        Scalar lowerBlack = new Scalar(0, 0, 0);
        Scalar upperBlack = new Scalar(180, 255, 50);
        Scalar lowerRed = new Scalar(0, 50, 50);
        Scalar upperRed = new Scalar(10, 255, 255);
        Scalar lowerGray = new Scalar(0, 0, 100);
        Scalar upperGray = new Scalar(180, 50, 255);

        // Create binary masks for black, red, and gray colors
        Mat blackMask = new Mat();
        Mat redMask = new Mat();
        Mat grayMask = new Mat();

        Core.inRange(reducedColors, lowerBlack, upperBlack, blackMask);
        Core.inRange(reducedColors, lowerRed, upperRed, redMask);
        Core.inRange(reducedColors, lowerGray, upperGray, grayMask);

        Scalar _color0 = HTML.toScalar(color0);
        Scalar _color1 = HTML.toScalar(color1);
        Scalar _color2 = HTML.toScalar(color2);

        // Create result image with black, red, and gray colors
        Mat result = new Mat(image.size(),image.type(), _color0);
        Core.add(result, _color0 , result, blackMask);
        Core.add(result, _color1, result, redMask);
        Core.add(result, _color2, result, grayMask);


        // Convert the result to grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(result, gray, Imgproc.COLOR_BGR2GRAY);

        // Apply a threshold to segment the main shape
        Mat threshold = new Mat();
        Imgproc.threshold(gray, threshold, 1, 255, Imgproc.THRESH_BINARY);

        // Find contours of the shapes
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(threshold, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Find the largest contour
        double maxArea = -1;
        int maxAreaIdx = -1;
        for (int i = 0; i < contours.size(); i++) {
            double area = Imgproc.contourArea(contours.get(i));
            if (area > maxArea) {
                maxArea = area;
                maxAreaIdx = i;
            }
        }
        // Draw the contours of the largest shape with a thick line
        Imgproc.drawContours(result, contours, maxAreaIdx, HTML.toScalar(contourColor), 4);


        return result;

    }
}
