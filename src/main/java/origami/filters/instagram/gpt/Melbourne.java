package origami.filters.instagram.gpt;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.core.Core.*;

public class Melbourne implements Filter {

    @Override
    public Mat apply(Mat image) {

        // Apply warmth by adjusting the red and green channels
        addWeighted(image, 1.2, new Mat(image.size(), image.type(), new Scalar(100, 100, 0)), -0.2, 0, image);

        // Convert the image to grayscale
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(grayImage, grayImage, Imgproc.COLOR_GRAY2BGR);

        // Blend the grayscale image with the original image
        double alpha = 0.7; // Adjust the opacity of the grayscale layer
        Core.addWeighted(image, alpha, grayImage, 0.3, 50, image);

        return image;

    }

}
