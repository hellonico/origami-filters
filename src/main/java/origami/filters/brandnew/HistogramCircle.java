package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.filters.FilterWithPalette;

import java.util.ArrayList;
import java.util.Arrays;

import static org.opencv.core.Core.*;
import static org.opencv.core.CvType.*;
import static org.opencv.imgproc.Imgproc.*;

public abstract class HistogramCircle extends FilterWithPalette implements Filter {

    public int numberOfColors = 256;

    public int getNumberOfColors() {
        return numberOfColors;
    }

    public void setNumberOfColors(int numberOfColors) {
        this.numberOfColors = numberOfColors;
    }

    @Override
    public Mat apply(Mat inputImage) {

        // Calculate the histogram of the input image
        MatOfFloat ranges = new MatOfFloat(0, numberOfColors);
        MatOfInt histSize = new MatOfInt(numberOfColors);
        MatOfInt channels = new MatOfInt(0);
        Mat hist = new Mat();
        calcHist(
                new ArrayList<>(Arrays.asList(inputImage)),
                channels,
                new Mat(),
                hist,
                histSize,
                ranges
        );

        // Find the maximum frequency in the histogram
        double maxFrequency = 0;
        MinMaxLocResult minMaxLocResult = minMaxLoc(hist);
        if (minMaxLocResult != null) {
            maxFrequency = minMaxLocResult.maxVal;
        }

        // Create the output gradient circles image
        Mat outputImage = new Mat(inputImage.rows(), inputImage.cols(), CV_8UC3, Scalar.all(0));

        draw(inputImage, hist, maxFrequency, outputImage);
        return outputImage;
    }

    public abstract void draw(Mat inputImage, Mat hist, double maxFrequency, Mat outputImage);

    public static class Grey extends HistogramCircle {
        public void draw(Mat inputImage, Mat hist, double maxFrequency, Mat outputImage) {
            // Draw gradient circles based on the input image colors
            for (int i = 0; i < numberOfColors; i++) {
                double frequency = hist.get(i, 0)[0];
                if (frequency > 0) {
                    int radius = (int) (Math.sqrt(frequency / maxFrequency) * Math.min(inputImage.rows(), inputImage.cols()) / 2);
                    // Scalar color = new Scalar(i, i, i);
                    Scalar color = this.palette.ratioColor((double) i / numberOfColors);
                    circle(outputImage, new Point(outputImage.cols() / 2, outputImage.rows() / 2), radius, color, -1);
                }
            }
        }
    }

    public static class Jet extends HistogramCircle {

        public int colorMap = 0;

        public int getColorMap() {
            return colorMap;
        }

        public void setColorMap(int colorMap) {
            this.colorMap = colorMap;
        }

        @Override
        public void draw(Mat inputImage, Mat hist, double maxFrequency, Mat outputImage) {

            // Draw gradient circles based on the input image colors
            for (int i = 0; i < numberOfColors; i++) {
                double frequency = hist.get(i, 0)[0];
                if (frequency > 0) {
                    int radius = (int) (Math.sqrt(frequency / maxFrequency) * Math.min(inputImage.rows(), inputImage.cols()) / 2);
                     Scalar color = new Scalar(i, i, i);
                    Imgproc.circle(outputImage, new Point(outputImage.cols() / 2, outputImage.rows() / 2), radius, color, -1);
                }
            }

            // Create a color map using the JET color map
            applyColorMap(outputImage, outputImage, colorMap);
        }
    }
}
