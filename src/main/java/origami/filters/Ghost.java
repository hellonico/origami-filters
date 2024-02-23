package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import origami.Filter;
import origami.filters.instagram.Sepia;

import static org.opencv.core.Core.addWeighted;
import static org.opencv.imgproc.Imgproc.*;

public class Ghost extends CrackedPaper implements Filter {
    double sigmaX = 30;

    public Ghost() {
        super.setPaper("paper2");
    }

    public double getSigmaX() {
        return sigmaX;
    }

    public void setSigmaX(double sigmaX) {
        this.sigmaX = sigmaX;
    }

    @Override
    public Mat apply(Mat inputImage) {

        // Create a grayscale copy of the input image
        Mat grayscaleImage = new Mat();
        cvtColor(inputImage, grayscaleImage, COLOR_BGR2GRAY);

        // Apply a Gaussian blur to the grayscale image
        Mat blurredImage = new Mat();
        GaussianBlur(grayscaleImage, blurredImage, new Size(0, 0), sigmaX);

        cvtColor(blurredImage, blurredImage, COLOR_GRAY2BGR);

        // Create a ghostly effect by combining the blurred image and the input image
        Mat ghostlyImage = new Mat();
        addWeighted(inputImage, 0.4, blurredImage, 0.6, 0, ghostlyImage);

        return super.apply(new Sepia.Gray().apply(blurredImage));

    }


}
