package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.utils.Utils;

import static org.opencv.core.CvType.*;

public class Brushed implements Filter {
    int blockSize = 35;
    int C = 10;

    int kernelSize = 17;

    Scalar desiredColor = new Scalar(0, 10, 103); // BGR values for red

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        C = c;
    }

    public int getKernelSize() {
        return kernelSize;
    }

    public void setKernelSize(int kernelSize) {
        this.kernelSize = kernelSize;
    }

    public String getDesiredColor() {
        return Utils.Scalar_String(desiredColor);
    }

    public void setDesiredColor(String desiredColor) {
        this.desiredColor = Utils.String_Scalar(desiredColor);
    }

    @Override
    public Mat apply(Mat inputImage) {
        // Convert the input image to grayscale
        Mat grayImage = new Mat();
        Imgproc.cvtColor(inputImage, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Apply the adaptive threshold to create a binary image
        Mat binaryImage = new Mat();
        Imgproc.adaptiveThreshold(grayImage, binaryImage, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY_INV, blockSize, C);

        // Dilate the binary image to thicken the strokes
        Mat dilatedImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(kernelSize, kernelSize));
        Imgproc.dilate(binaryImage, dilatedImage, kernel);

        // Convert the dilated image to color for blending
        Mat colorDilated = new Mat();
        Imgproc.cvtColor(dilatedImage, colorDilated, Imgproc.COLOR_GRAY2BGR);


        // Apply the color burn blending mode
        Mat outputImage = new Mat();
        Core.divide(inputImage, colorDilated, outputImage, 255, CV_8UC3);
        Core.multiply(outputImage, desiredColor, outputImage);

        return outputImage;
    }
}
