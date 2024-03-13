package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.annotations.Parameter;
import origami.colors.HTML;
import origami.filters.FilterWithPalette;

import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgproc.Imgproc.FILLED;

public abstract class Histogram extends FilterWithPalette implements Filter {

    public int bins = 20;

    String bgColor = "#FFFFFF";

    @Parameter(description = "One of heart, circle, rect")
    public String shape = "heart";

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

        int histWidth = image.width();
        int histHeight = image.height();
        Mat histImage = new Mat(histHeight, histWidth, CvType.CV_8UC3, HTML.toScalar(bgColor));

        int binWidth = (int) Math.round(histWidth / histSize.get(0, 0)[0]);

        draw(histSize, hist, histHeight, histImage, binWidth);

        return histImage;
    }

    protected abstract void draw(MatOfInt histSize, Mat hist, int histHeight, Mat histImage, int binWidth);

    public static class Circling extends Histogram {

        public int radiusSize = 10;
        public int circleSize = 1;
        public int X;

        public int Y;
        public int mainCircleThickness = 1;

        public int getMainCircleThickness() {
            return mainCircleThickness;
        }

        public void setMainCircleThickness(int mainCircleThickness) {
            this.mainCircleThickness = mainCircleThickness;
        }

        public int getX() {
            return X;
        }

        public void setX(int x) {
            X = x;
        }

        public int getY() {
            return Y;
        }

        public void setY(int y) {
            Y = y;
        }

        public int getCircleSize() {
            return circleSize;
        }

        public void setCircleSize(int circleSize) {
            this.circleSize = circleSize;
        }

        public int getRadiusSize() {
            return radiusSize;
        }

        public void setRadiusSize(int radiusSize) {
            this.radiusSize = radiusSize;
        }

        public void draw(MatOfInt histSize, Mat hist, int histHeight, Mat histImage, int binWidth) {

            // Calculate the center and radius of the main circle
            int centerX = X == 0 ? histImage.cols() / 2 : X;
            int centerY = Y == 0 ? histImage.rows() / 2 : Y;
            int mainCircleRadius = Math.min(centerX, centerY) - radiusSize;

            double histMaxValue = Core.minMaxLoc(hist).maxVal;

            // Calculate the angle for each bin based on the number of bins
            double anglePerBin = 2 * Math.PI / histSize.get(0, 0)[0];

            // Draw the main circle
            Imgproc.circle(histImage, new org.opencv.core.Point(centerX, centerY), mainCircleRadius, new Scalar(255, 255, 255), mainCircleThickness);

            // Draw the filled circles representing the bins
            for (int i = 0; i < histSize.get(0, 0)[0]; i++) {
                int binValue = (int) Math.round(hist.get(i, 0)[0]);
                double binRadius = mainCircleRadius + binValue; // Adjust the factor to control the size of the small circles

                // Calculate the angle for the current bin
                double angle = i * anglePerBin;

                // Calculate the coordinates of the center of the current bin circle
                int circleCenterX = centerX + (int) (binRadius * Math.cos(angle));
                int circleCenterY = centerY + (int) (binRadius * Math.sin(angle));

                // palette color
                Scalar paletteColor = this.palette.ratioColor((double) i / bins);

                // Draw the filled circle representing the current bin
                // Imgproc.circle(histImage, new org.opencv.core.Point(circleCenterX, circleCenterY), binValue, paletteColor, FILLED);

                // Define the color gradient for the current bin
                Scalar endColor = new Scalar(255, 255, 255); // Ending color of the gradient

                // Calculate the intermediate color based on the current bin value
                double t = (double) binValue / histMaxValue;
                Scalar currentColor = new Scalar(
                        (1 - t) * paletteColor.val[0] + t * endColor.val[0],
                        (1 - t) * paletteColor.val[1] + t * endColor.val[1],
                        (1 - t) * paletteColor.val[2] + t * endColor.val[2]
                );

                // Draw the filled circle representing the current bin with gradient color
                Size circleSize = new Size((double) binValue / getCircleSize(), (double) binValue / getCircleSize());
                Imgproc.ellipse(histImage, new org.opencv.core.Point(circleCenterX, circleCenterY), circleSize, 0, 0, 360, currentColor, FILLED);
            }

        }
    }

    public static class Gradient extends Histogram {
        public void draw(MatOfInt histSize, Mat hist, int histHeight, Mat histImage, int binWidth) {
            // Draw the histogram rectangles with gradient fill
            for (int i = 0; i < histSize.get(0, 0)[0]; i++) {
                int binHeight = (int) Math.round(hist.get(i, 0)[0]);
                int size = Math.min(binHeight, binWidth);

                // Calculate the position of the top-left corner of the rectangle
                int startX = i * binWidth;
                int startY = histHeight - binHeight;

                // Calculate the end position of the rectangle
                int endX = (i + 1) * binWidth;
                int endY = histHeight;

                Scalar startColor = this.palette.ratioColor((double) i / bins);
                // Interpolate colors for gradient fill
//            Scalar startColor = new Scalar(0, 255, 0); // Starting color of the gradient
                Scalar endColor = new Scalar(255, 255, 255);   // Ending color of the gradient

                for (int j = 0; j < size; j++) {
                    // Calculate the interpolation factor (range: 0.0 - 1.0)
                    double t = (double) j / size;

                    // Interpolate the color
                    int b = (int) ((1 - t) * startColor.val[0] + t * endColor.val[0]);
                    int g = (int) ((1 - t) * startColor.val[1] + t * endColor.val[1]);
                    int r = (int) ((1 - t) * startColor.val[2] + t * endColor.val[2]);
                    Scalar color = new Scalar(b, g, r);

                    // Draw the gradient-filled rectangle
                    rectangle(histImage, new org.opencv.core.Point(startX, startY + j),
                            new org.opencv.core.Point(endX, startY + j + 1), color, FILLED);
                }

                // Draw the border of the rectangle
                Scalar borderColor = new Scalar(255, 255, 255); // Border color
                rectangle(histImage, new org.opencv.core.Point(startX, startY),
                        new org.opencv.core.Point(endX, endY), borderColor, 1);
            }
        }

    }

    public static class Heart extends Histogram {


        public void draw(MatOfInt histSize, Mat hist, int histHeight, Mat histImage, int binWidth) {

            // Draw the histogram hearts on the image
            for (int i = 0; i < histSize.get(0, 0)[0]; i++) {
                int binHeight = (int) Math.round(hist.get(i, 0)[0]);
                int size = Math.min(binHeight, binWidth);

                // Calculate the position of the top-left corner of the heart
                int startX = i * binWidth;
                int startY = histHeight - binHeight;

                // Draw the heart
                int midX = startX + size / 2;
                int midY = startY + size / 2;
                int quarterWidth = size / 4;
                int halfHeight = size / 2;

                Scalar color = this.palette.ratioColor((double) i / bins);

                // Top-left curve
                ellipse(histImage, new Point(midX - quarterWidth, midY - halfHeight),
                        new Size(quarterWidth, halfHeight), 180, 0, 180, color, -1);
                // Top-right curve
                ellipse(histImage, new Point(midX + quarterWidth, midY - halfHeight),
                        new Size(quarterWidth, halfHeight), 180, 0, 180, color, -1);

                // Bottom triangle
                Point[] trianglePoints = {
                        new Point(midX - 2 * quarterWidth, midY - halfHeight),
                        new Point(midX + 2 * quarterWidth, midY - halfHeight),
                        new Point(midX, midY + size / 2)
                };
                MatOfPoint triangleMat = new MatOfPoint(trianglePoints);
                fillConvexPoly(histImage, triangleMat, color);
            }
        }
    }

    public static class Circle extends Histogram {

        public void draw(MatOfInt histSize, Mat hist, int histHeight, Mat histImage, int binWidth) {
            for (int i = 0; i < histSize.get(0, 0)[0]; i++) {
                int binHeight = (int) Math.round(hist.get(i, 0)[0]);
                int radius = Math.min(binHeight, binWidth) / 2;

                // Calculate the center position of the circle
                int centerX = (i * binWidth) + (binWidth / 2);
                int centerY = histHeight - radius;

                // Draw the circle
                circle(histImage, new Point(centerX, centerY), radius, this.palette.ratioColor((double) i / bins), -1);
            }
        }
    }

    public static class Rect extends Histogram {
        public void draw(MatOfInt histSize, Mat hist, int histHeight, Mat histImage, int binWidth) {
            for (int i = 0; i < histSize.get(0, 0)[0]; i++) {
                int binHeight = (int) Math.round(hist.get(i, 0)[0]);
                rectangle(
                        histImage,
                        new Point(i * binWidth, histHeight),
                        new Point((i + 1) * binWidth, histHeight - binHeight),
                        this.palette.ratioColor((double) i / bins),
                        FILLED
                );
            }
        }
    }

}
