package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.Arrays;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class ShadowFilter implements Filter {

    // Apply shading effect using a distant light source
    public int lightSourceX = 100;  // X-coordinate of the light source (negative for distant light)
    public int lightSourceY = 100;  // Y-coordinate of the light source (negative for distant light)
    public double shadingFactor = 0.5;  // Shading factor between 0.0 and 1.0 (adjust as needed)

    public int getLightSourceX() {
        return lightSourceX;
    }

    public void setLightSourceX(int lightSourceX) {
        this.lightSourceX = lightSourceX;
    }

    public int getLightSourceY() {
        return lightSourceY;
    }

    public void setLightSourceY(int lightSourceY) {
        this.lightSourceY = lightSourceY;
    }

    public double getShadingFactor() {
        return shadingFactor;
    }

    public void setShadingFactor(double shadingFactor) {
        this.shadingFactor = shadingFactor;
    }

    @Override
    public Mat apply(Mat image) {
        // Convert the image to grayscale
        Mat grayImage = new Mat();
        cvtColor(image, grayImage, COLOR_BGR2GRAY);

        Mat shadingImage = new Mat();
        subtract(grayImage, new Scalar(255 * shadingFactor), shadingImage);

        // Create a 3-channel image for merging the shaded image with the original color image
        Mat resultImage = new Mat();

        List<Mat> channels = Arrays.asList(shadingImage, shadingImage, shadingImage);
        merge(channels, resultImage);

        // Add the shaded image to the original color image
        add(resultImage, image, resultImage);
        return resultImage;

    }
}
