package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.colors.HTML;

import java.util.Random;

public class Shepard implements Filter {

    Scalar specifiedColor = new Scalar(255, 255, 255);

    public String getSpecifiedColor() {
        return HTML.toHTML(specifiedColor);
    }

    public void setSpecifiedColor(String specifiedColor) {
        this.specifiedColor = HTML.toScalar(specifiedColor);
    }

    public int thickness = 1;
    public int numLines = 50;

    public int low = 127;

    public int high = 255;

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getNumLines() {
        return numLines;
    }

    public void setNumLines(int numLines) {
        this.numLines = numLines;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    @Override
    public Mat apply(Mat image) {
        Mat invertedImage = new Mat();
        Core.bitwise_not(image, invertedImage);

        Mat grayImage = new Mat();
        Imgproc.cvtColor(invertedImage, grayImage, Imgproc.COLOR_BGR2GRAY);

        Mat bwImage = new Mat();
        Imgproc.threshold(grayImage, bwImage, low, high, Imgproc.THRESH_BINARY);

        Random random = new Random();
        int height = bwImage.rows();
        int width = bwImage.cols();

        for (int i = 0; i < numLines; i++) {
            int y = random.nextInt(height);
            Imgproc.line(bwImage, new Point(0, y), new Point(width, y), new Scalar(0), thickness);
        }

        // Create a mask for the black pixels
        Mat mask = new Mat();
        Core.compare(bwImage, new Scalar(0), mask, Core.CMP_EQ);

        // Create a 3-channel color image using the specified color
        Mat colorImage = new Mat(bwImage.rows(), bwImage.cols(), CvType.CV_8UC3, specifiedColor);

        // Copy the color image to the result using the mask
        Mat result = new Mat();
        Core.copyTo(colorImage, result, mask);

        return result;
    }
}
