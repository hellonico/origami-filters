package origami.filters.brandnew;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import origami.Filter;
import origami.filters.instagram.Sepia;

import static org.opencv.core.Core.addWeighted;
import static org.opencv.imgproc.Imgproc.*;

public class Ghost extends CrackedPaper implements Filter {
    public double sigmaX = 30;

    public double blur = 1;

    public Ghost() {
        super.setPaper("paper2");
    }

    public double getSigmaX() {
        return sigmaX;
    }

    public void setSigmaX(double sigmaX) {
        this.sigmaX = sigmaX;
    }

    public double getBlur() {
        return blur;
    }

    public void setBlur(double blur) {
        this.blur = blur;
    }

    @Override
    public Mat apply(Mat inputImage) {

        // Create a grayscale copy of the input image
        Mat grayscaleImage = new Mat();
        cvtColor(inputImage, grayscaleImage, COLOR_BGR2GRAY);

        // Apply a Gaussian blur to the grayscale image
        Mat blurredImage = new Mat();
        GaussianBlur(grayscaleImage, blurredImage, new Size(blur, blur), sigmaX);

        cvtColor(blurredImage, blurredImage, COLOR_GRAY2BGR);

        // Create a ghostly effect by combining the blurred image and the input image
        Mat ghostlyImage = new Mat();
        addWeighted(inputImage, 0.4, blurredImage, 0.6, gamma, ghostlyImage);

        return super.apply(new Sepia.Gray().apply(ghostlyImage));

    }


}
