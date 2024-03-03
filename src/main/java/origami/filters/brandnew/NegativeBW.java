package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.imgproc.Imgproc.*;

public class NegativeBW implements Filter {
    public double darkRatio = 0.2;

    public double getDarkRatio() {
        return darkRatio;
    }

    public void setDarkRatio(double darkRatio) {
        this.darkRatio = darkRatio;
    }

    @Override
    public Mat apply(Mat image) {
        // Convert the image to grayscale
        Mat grayscaleImage = new Mat();
        cvtColor(image, grayscaleImage, COLOR_BGR2GRAY);

        // Create a negative image
        Mat negativeImage = new Mat();
        Core.bitwise_not(grayscaleImage, negativeImage);

        // Create a dark, black and white image
        Mat darkImage = new Mat();
        Core.multiply(negativeImage, new Scalar(darkRatio), darkImage);

        return darkImage;

    }
}
