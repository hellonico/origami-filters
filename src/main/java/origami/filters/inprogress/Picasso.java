package origami.filters.inprogress;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.colors.HTML;
import origami.filters.FilterWithPalette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class Picasso extends FilterWithPalette implements Filter {
    public double getCannyLow() {
        return cannyLow;
    }

    public void setCannyLow(double cannyLow) {
        this.cannyLow = cannyLow;
    }

    public double getCannyHigh() {
        return cannyHigh;
    }

    public void setCannyHigh(double cannyHigh) {
        this.cannyHigh = cannyHigh;
    }

    public int getKernelSize() {
        return kernelSize;
    }

    public void setKernelSize(int kernelSize) {
        this.kernelSize = kernelSize;
    }

    public int getSigma() {
        return sigma;
    }

    public void setSigma(int sigma) {
        this.sigma = sigma;
    }

    public int getNumShapes() {
        return numShapes;
    }

    public void setNumShapes(int numShapes) {
        this.numShapes = numShapes;
    }

    public String getBgColor() {
        return HTML.toHTML(bgColor);
    }

    public void setBgColor(String bgColor) {
        this.bgColor = HTML.toScalar(bgColor);
    }

    double cannyLow = 1;
    double cannyHigh = 250;

    int kernelSize = 71;
    int sigma = 0;
    int numShapes = 15;

    Scalar bgColor = HTML.toScalar("#ffffff");

    @Override
    public Mat apply(Mat inputImage) {

        // Convert the image to grayscale
        Mat grayImage = new Mat();
        cvtColor(inputImage, grayImage, COLOR_BGR2GRAY);

        // Perform Canny edge detection
        Mat edges = new Mat();
        Canny(grayImage, edges, cannyLow , cannyHigh);


        // Apply Gaussian blur to reduce noise
        Mat blurredImage = new Mat();
        GaussianBlur(edges, blurredImage, new Size(kernelSize, kernelSize), sigma);

        // Find contours in the image
//        Mat hierarchy = new Mat();
        MatOfPoint mainContour = null;
        double maxContourArea = 0;

//        Mat contours = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(blurredImage, contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE);

        // Sort contours by area in descending order
        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint c1, MatOfPoint c2) {
                double area1 = Imgproc.contourArea(c1);
                double area2 = Imgproc.contourArea(c2);
                return Double.compare(area2, area1);
            }
        });

        // Draw the five biggest contours
        Mat resultImage = new Mat(inputImage.size(), inputImage.type(), bgColor);
        int numContoursToDraw = Math.min(contours.size(), numShapes);
        for (int i = 0; i < numContoursToDraw; i++) {
            MatOfPoint contour = contours.get(i);

//            Scalar color = new Scalar(0, 0, 255);
            Scalar color = palette.ratioColor((double) i /numContoursToDraw);
//            Scalar color = palette.ratioColor(contour.size()/ima);
            Imgproc.drawContours(resultImage, Collections.singletonList(contour), -1, color, Imgproc.FILLED);
        }


        return resultImage;
    }

}
