package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class DiffusingLightWithROI implements Filter {
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

        Rect roi = new Rect(100, 100, 200, 200);
        // Create a blurred version of the input image
        Mat roiImage = inputImage.submat(roi);

        // Create a blurred version of the ROI
        Mat blurredImage = new Mat();
        GaussianBlur(roiImage, blurredImage, new Size(blurSize, blurSize), 10);

        // Subtract the blurred image from the ROI
        Mat diffusedRoiImage = new Mat();
        subtract(roiImage, blurredImage, diffusedRoiImage);

        // Increase the contrast of the diffused ROI image
        add(diffusedRoiImage, new Scalar(contrastFix, contrastFix, contrastFix), diffusedRoiImage);

        // Copy the diffused ROI image back to the original image
        diffusedRoiImage.copyTo(inputImage.submat(roi));

        // Convert the image to the appropriate type for display
        inputImage.convertTo(inputImage, CvType.CV_8UC3);

        return inputImage;
    }
}
