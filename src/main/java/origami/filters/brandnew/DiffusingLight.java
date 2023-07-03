package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class DiffusingLight implements Filter {
    public int blurSize = 0 ;
    public int contrastFix = 75;

    public int getBlurSize() {
        return blurSize;
    }

    public void setBlurSize(int blurSize) {
        this.blurSize = blurSize;
    }

    public int getContrastFix() {
        return contrastFix;
    }

    public void setContrastFix(int contrastFix) {
        this.contrastFix = contrastFix;
    }

    @Override
    public Mat apply(Mat inputImage) {
        // Create a blurred version of the input image
        Mat blurredImage = new Mat();
        Imgproc.GaussianBlur(inputImage, blurredImage, new Size(blurSize, 0), 10);

        // Subtract the blurred image from the input image
        Mat diffusedImage = new Mat();
        Core.subtract(inputImage, blurredImage, diffusedImage);

        // Increase the contrast of the diffused image
        Core.add(diffusedImage, new Scalar(contrastFix, contrastFix, contrastFix), diffusedImage);

        // Convert the image to the appropriate type for display
        diffusedImage.convertTo(diffusedImage, CvType.CV_8UC3);

        return diffusedImage;
    }
}
