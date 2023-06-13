package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.Arrays;
import java.util.List;

public class ShadowFilter implements Filter {
    @Override
    public Mat apply(Mat image) {
        // Convert the image to grayscale
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Apply shading effect using a distant light source
        int lightSourceX = 100;  // X-coordinate of the light source (negative for distant light)
        int lightSourceY = 100;  // Y-coordinate of the light source (negative for distant light)
        double shadingFactor = 0.5;  // Shading factor between 0.0 and 1.0 (adjust as needed)

        Mat shadingImage = new Mat();
        Core.subtract(grayImage, new Scalar(255 * shadingFactor), shadingImage);

        // Create a 3-channel image for merging the shaded image with the original color image
        Mat resultImage = new Mat();

        List<Mat> channels = Arrays.asList(shadingImage, shadingImage, shadingImage);
        Core.merge(channels, resultImage);

        // Add the shaded image to the original color image
        Core.add(resultImage, image, resultImage);
        return resultImage;

    }
}
