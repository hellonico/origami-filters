package origami.filters;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.colors.HTML;

import java.util.Arrays;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgproc.Imgproc.FILLED;

public class Histogram extends FilterWithPalette implements Filter {

    int bins = 20;

    String bgColor = "#FFFFFF";

    boolean circles = true;

    public boolean isCircles() {
        return circles;
    }

    public void setCircles(boolean circles) {
        this.circles = circles;
    }

    public Histogram() {
        this.setPaletteName("sunrise");
    }

    public int getBins() {
        return bins;
    }

    public void setBins(int bins) {
        this.bins = bins;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    public Mat apply(Mat image) {
        Mat grayImage = new Mat();
        cvtColor(image, grayImage, COLOR_BGR2GRAY);

        MatOfInt histSize = new MatOfInt(bins); // Number of bins
        MatOfFloat histRange = new MatOfFloat(0, 256); // Range of values

        Mat hist = new Mat();
        calcHist(
                List.of(grayImage),
                new MatOfInt(0), // Channel index (0 for grayscale)
                new Mat(), // Mask (null for the entire image)
                hist,
                histSize,
                histRange
        );

        normalize(hist, hist, 0, image.rows(), NORM_MINMAX);

        //Mat histImage = new Mat(mat.size(), HTML.toHTML("#fcfcfc"));

        int histWidth = image.width();
        int histHeight = image.height();
        Mat histImage = new Mat(histHeight, histWidth, CvType.CV_8UC3, HTML.toScalar(bgColor));

        int binWidth = (int) Math.round(histWidth / histSize.get(0, 0)[0]);

        if (!circles) {
            for (int i = 0; i < histSize.get(0, 0)[0]; i++) {
                int binHeight = (int) Math.round(hist.get(i, 0)[0]);
                rectangle(
                        histImage,
                        new org.opencv.core.Point(i * binWidth, histHeight),
                        new org.opencv.core.Point((i + 1) * binWidth, histHeight - binHeight),
                        this.palette.ratioColor((double) i / bins), //new Scalar(0, 255, 0), // Color of the bars (green in this case)
                        FILLED
                );
            }
        } else {

            // Draw the histogram circles on the image
            for (int i = 0; i < histSize.get(0, 0)[0]; i++) {
                int binHeight = (int) Math.round(hist.get(i, 0)[0]);
                int radius = Math.min(binHeight, binWidth) / 2;

                // Calculate the center position of the circle
                int centerX = (i * binWidth) + (binWidth / 2);
                int centerY = histHeight - radius;

                // Draw the circle
                Imgproc.circle(histImage, new org.opencv.core.Point(centerX, centerY), radius, this.palette.ratioColor((double) i / bins), -1);
            }
        }
        return histImage;
    }
}
