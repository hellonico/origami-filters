package origami.filters.inprogress;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import origami.Filter;

import java.util.Arrays;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class ShadowFilter2 implements Filter {
    @Override
    public Mat apply(Mat image) {
        // Convert the image to grayscale
        Mat grayImage = new Mat();
        cvtColor(image, grayImage, COLOR_BGR2GRAY);

        // Apply the Sobel operator
        Mat gradientX = new Mat();
        Mat gradientY = new Mat();
        Sobel(grayImage, gradientX, CvType.CV_32F, 1, 0, 3, 1, 0, BORDER_DEFAULT);
        Sobel(grayImage, gradientY, CvType.CV_32F, 0, 1, 3, 1, 0, BORDER_DEFAULT);
//
//        Imgproc.Sobel(grayImage, gradientX, CvType.CV_32F, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
//        Imgproc.Sobel(grayImage, gradientY, CvType.CV_32F, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT);

        // Calculate the magnitude of gradients
        Mat magnitude = new Mat();
        magnitude(gradientX, gradientY, magnitude);

        // Normalize the magnitude
        normalize(magnitude, magnitude, 0, 255, NORM_MINMAX, CvType.CV_8UC1);

        // Invert the magnitude to create the shading effect
        subtract(Mat.zeros(magnitude.size(), CvType.CV_8UC1), magnitude, magnitude);
//        subtract(new Scalar(255), magnitude, magnitude);

        // Scale the shading effect
        double shadingFactor = 20.5;  // Adjust the shading factor as needed
        Core.multiply(magnitude, new Scalar(shadingFactor), magnitude);

        // Create a 3-channel image for merging the shaded image with the original color image
        Mat resultImage = new Mat();
        List<Mat> channels = Arrays.asList(magnitude, magnitude, magnitude);
        merge(channels, resultImage);

        // Add the shaded image to the original color image
        add(resultImage, image, resultImage);

        return resultImage;
    }
}
