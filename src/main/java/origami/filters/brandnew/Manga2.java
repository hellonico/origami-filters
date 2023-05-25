package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.colors.HTML;
import origami.colors.Palette;
import origami.colors.RGB;
import origami.filters.FilterWithPalette;

import java.util.*;

import static org.opencv.imgproc.Imgproc.*;

public class Manga2 extends FilterWithPalette implements Filter {

    public double kernelSize = 17;
    public double cannyLow = 5;
    public double cannyHigh = 100;

    public double filterD = 3;
    public double sigmaColor = 30;
    public double sigmaSpace = 75;


    public double getKernelSize() {
        return kernelSize;
    }

    public void setKernelSize(double kernelSize) {
        this.kernelSize = kernelSize;
    }

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

    public double getFilterD() {
        return filterD;
    }

    public void setFilterD(double filterD) {
        this.filterD = filterD;
    }

    public double getSigmaColor() {
        return sigmaColor;
    }

    public void setSigmaColor(double sigmaColor) {
        this.sigmaColor = sigmaColor;
    }

    public double getSigmaSpace() {
        return sigmaSpace;
    }

    public void setSigmaSpace(double sigmaSpace) {
        this.sigmaSpace = sigmaSpace;
    }

    public Manga2() {
        super();
    }

    public Mat apply(Mat inputImage) {
        // Convert the image to grayscale
        Mat grayImage = new Mat();
        cvtColor(inputImage, grayImage, COLOR_BGR2GRAY);

        // Apply a bilateral filter to smooth the image
        Mat smoothedImage = new Mat();
        bilateralFilter(grayImage, smoothedImage, 3, 30, 75);

        // Perform edge detection using Canny
        Mat edges = new Mat();
        Canny(smoothedImage, edges, cannyLow, cannyHigh);

        // Perform morphological closing to close gaps in between object edges
        Mat closedEdges = new Mat();

        Mat kernel = getStructuringElement(MORPH_RECT, new Size(kernelSize, kernelSize));
        morphologyEx(edges, closedEdges, MORPH_CLOSE, kernel);


        // Find contours of the closed edges
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        findContours(closedEdges, contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

        // Create a blank image to draw the manga-like effect
        Mat mangaImage = new Mat(inputImage.size(), CvType.CV_8UC3);
        mangaImage.setTo(RGB.white);

        // Iterate through each contour and draw it on the manga image
        for (MatOfPoint matOfPoint : contours) {
            MatOfPoint2f contour = new MatOfPoint2f();
            matOfPoint.convertTo(contour, CvType.CV_32F);

            // Approximate the contour with a polygon
            double epsilon = 0.001 * arcLength(contour, true);
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            approxPolyDP(contour, approxCurve, epsilon, true);

            RotatedRect boundingRect = Imgproc.minAreaRect(approxCurve);


            // Convert the polygon back to MatOfPoint
            MatOfPoint polygon = new MatOfPoint(approxCurve.toArray());

            // Draw the filled polygon on the manga image

            fillPoly(mangaImage, Collections.singletonList(polygon), palette.ratioColor(boundingRect.size.area()/inputImage.size().area()));

        }

        return mangaImage;
    }

}
