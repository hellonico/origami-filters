package origami.filters.brandnew;

import org.opencv.core.*;
import origami.Filter;
import origami.filters.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class ArtisticFilter implements Filter {

    Scalar colorOverride = new Scalar(0.8, 0.3, 0.8);

    Scalar gradientStart = new Scalar(255, 255, 255);

    Scalar gradientEnd = new Scalar(0, 0, 255);

    public double gradientWeight = 0.3;

    public String getGradientStart() {
        return Utils.Scalar_String(gradientStart);
    }

    public void setGradientStart(String gradientStart) {
        this.gradientStart = Utils.String_Scalar(gradientStart);
    }

    public String getGradientEnd() {
        return Utils.Scalar_String(gradientEnd);
    }

    public void setGradientEnd(String gradientEnd) {
        this.gradientEnd = Utils.String_Scalar(gradientEnd);
    }

    public double getGradientWeight() {
        return gradientWeight;
    }

    public void setGradientWeight(double gradientWeight) {
        this.gradientWeight = gradientWeight;
    }

    public boolean isGradient() {
        return gradient;
    }

    public void setGradient(boolean gradient) {
        this.gradient = gradient;
    }

    public boolean gradient;

    public String getColorOverride() {
        return Utils.Scalar_String(colorOverride);
    }

    public void setColorOverride(String colorOverride) {
        this.colorOverride = Utils.String_Scalar(colorOverride);
    }

    @Override
    public Mat apply(Mat inputImage) {
        // Convert the image to the Lab color space
        Mat labImage = new Mat();
        cvtColor(inputImage, labImage, COLOR_BGR2Lab);

        // Split the Lab image channels
        List<Mat> labChannels = new ArrayList<>();
        split(labImage, labChannels);

        // Enhance the saturation of the 'a' and 'b' channels
        Mat aChannel = labChannels.get(1);
        Mat bChannel = labChannels.get(2);
        addWeighted(aChannel, 1.5, aChannel, 0, 0, aChannel);
        addWeighted(bChannel, 1.5, bChannel, 0, 0, bChannel);

        // Merge the modified Lab channels back into an image
        merge(labChannels, labImage);

        // Convert the image back to the BGR color space
        Mat outputImage = new Mat();
        cvtColor(labImage, outputImage, COLOR_Lab2BGR);

        // Adjust the contrast of the output image
        convertScaleAbs(outputImage, outputImage, 1.5, 0);

        // Dodge and burn to enhance highlights and shadows
        Mat mask = new Mat();
        cvtColor(inputImage, mask, COLOR_BGR2GRAY);
        threshold(mask, mask, 128, 255, THRESH_BINARY_INV);

        Mat highlightMask = new Mat();
        dilate(mask, highlightMask, new Mat(), new Point(-1, -1), 3);
        blur(highlightMask, highlightMask, new Size(30, 30));

        Mat shadowMask = new Mat();
        erode(mask, shadowMask, new Mat(), new Point(-1, -1), 3);
        blur(shadowMask, shadowMask, new Size(30, 30));

        add(outputImage, new Scalar(50, 50, 50), outputImage, highlightMask);
        subtract(outputImage, new Scalar(50, 50, 50), outputImage, shadowMask);


        // Adjust the overall color tone to pink/magenta
        // multiply(outputImage, new Scalar(0.8, 0.3, 0.8), outputImage);
        // Adjust the overall color tone to blue
        if (!gradient) {
            multiply(outputImage, colorOverride, outputImage);
        } else {

            // Generate a gradient background
            Mat gradient = generateGradient(inputImage.size(), gradientStart, gradientEnd);

            // Blend the output image with the gradient background
            Core.addWeighted(outputImage, 0.5, gradient, gradientWeight, 0, outputImage);
        }

        return outputImage;
    }


    private static Mat generateGradient(Size size, Scalar startColor, Scalar endColor) {
        Mat gradient = new Mat(size, CvType.CV_8UC3);

        int rows = (int) size.height;
        int cols = (int) size.width;

        double stepR = (endColor.val[0] - startColor.val[0]) / rows;
        double stepG = (endColor.val[1] - startColor.val[1]) / rows;
        double stepB = (endColor.val[2] - startColor.val[2]) / rows;

        for (int i = 0; i < rows; i++) {
            double r = startColor.val[0] + (stepR * i);
            double g = startColor.val[1] + (stepG * i);
            double b = startColor.val[2] + (stepB * i);

            double[] color = {b, g, r};
            gradient.row(i).setTo(new Scalar(color));
        }

        return gradient;
    }

}
