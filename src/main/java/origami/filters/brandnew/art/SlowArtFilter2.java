package origami.filters.brandnew.art;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import origami.Filter;

import java.util.Random;

import static org.opencv.core.Core.*;
import static org.opencv.core.Core.bitwise_and;
import static org.opencv.imgproc.Imgproc.*;

public class SlowArtFilter2 implements Filter {
    public double min = (double) 255 / 3;
    public double max = 150;
    public int aperture = 5;
    public boolean gradient = true;

    public int d = 9;
    public int signmaColor = 75;
    public int sigmaSpace = 75;

    public int blur = 7;

    public boolean invert = true;


    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public int getAperture() {
        return aperture;
    }

    public void setAperture(int aperture) {
        this.aperture = aperture;
    }

    public boolean isGradient() {
        return gradient;
    }

    public void setGradient(boolean gradient) {
        this.gradient = gradient;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getSignmaColor() {
        return signmaColor;
    }

    public void setSignmaColor(int signmaColor) {
        this.signmaColor = signmaColor;
    }

    public int getSigmaSpace() {
        return sigmaSpace;
    }

    public void setSigmaSpace(int sigmaSpace) {
        this.sigmaSpace = sigmaSpace;
    }

    public int getBlur() {
        return blur;
    }

    public void setBlur(int blur) {
        this.blur = blur;
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public double getRatio2() {
        return ratio2;
    }

    public void setRatio2(double ratio2) {
        this.ratio2 = ratio2;
    }

    public double ratio2 = 0.3;


    @Override
    public Mat apply(Mat src) {

        // Apply bilateral filter to smooth the image while keeping edges sharp
        Mat bilateral = new Mat();
        bilateralFilter(src, bilateral, d, signmaColor, sigmaSpace);

        // Add randomness: Random color shift
        Mat randomized = new NoisyBlock().apply(bilateral);

        // Convert to grayscale
        Mat gray = new Mat();
        cvtColor(src, gray, COLOR_BGR2GRAY);

        // Apply median blur to the grayscale image
        medianBlur(gray, gray, blur);

        // Detect edges using Canny edge detector
        Mat edges = new Mat();
        Canny(gray, edges, min, max, aperture, gradient);

        // Invert the edges to use for masking
        if (invert) {
            bitwise_not(edges, edges);
        }

        // Convert edges to 3-channel image
        Mat edgesColor = new Mat();
        cvtColor(edges, edgesColor, COLOR_GRAY2BGR);

        // Combine edges with the original image using a bitwise AND
        Mat cartoon = new Mat();
        addWeighted(randomized, ratio2, edgesColor, 1-ratio2, 0, cartoon); // Blend the artistic effect more

        return cartoon;
    }

}
