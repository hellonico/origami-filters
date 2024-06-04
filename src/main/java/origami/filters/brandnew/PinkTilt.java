package origami.filters.brandnew;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import origami.Filter;
import origami.colors.RGB;

import static org.opencv.core.Core.addWeighted;
import static org.opencv.imgproc.Imgproc.*;

public class PinkTilt implements Filter {
    String color_s = "indian_red";
    Scalar color = RGB.toScalar(color_s);

    public String getColor() {
        return color_s;
    }

    public void setColor(String color_s) {
        this.color_s = color_s;
        this.color = RGB.toScalar(color_s);
    }

    double alpha = 0.7;
    double beta = 0.7;

    double gamma = 10;

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public Mat apply(Mat src) {
        Mat gray = new Mat();
        cvtColor(src, gray, COLOR_BGR2GRAY);

        // Apply CLAHE (Contrast Limited Adaptive Histogram Equalization)
        Mat enhanced = new Mat();
        createCLAHE().apply(gray, enhanced);

        // Convert the enhanced grayscale image back to BGR
        Mat enhancedColor = new Mat();
        cvtColor(enhanced, enhancedColor, COLOR_GRAY2BGR);

        // Create a pink tint by adding more red and blue
        Mat pinkTint = new Mat(enhancedColor.size(), CvType.CV_8UC3, color);
        addWeighted(enhancedColor, alpha, pinkTint, beta, gamma, enhancedColor);

        return enhancedColor;

    }
}
