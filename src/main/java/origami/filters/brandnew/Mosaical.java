package origami.filters.brandnew;

import org.opencv.core.*;
import origami.Filter;
import origami.colors.HTML;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.ellipse;
import static org.opencv.imgproc.Imgproc.rectangle;

public class Mosaical implements Filter {

    public int squareSize = 10;  // Adjust the square size as needed
    public int radiusRatio = 2;

    public int getSquareSize() {
        return squareSize;
    }

    public void setSquareSize(int squareSize) {
        this.squareSize = squareSize;
    }

    public int getRadiusRatio() {
        return radiusRatio;
    }

    public void setRadiusRatio(int radiusRatio) {
        this.radiusRatio = radiusRatio;
    }

    public Scalar color = HTML.toScalar("black");

    public String getColor() {
        return HTML.toHTML(color);
    }

    public void setColor(String color) {
        this.color = HTML.toScalar(color);
    }

    @Override
    public Mat apply(Mat image) {

        // Create the mosaic with rounded corners
        return createMosaicWithRoundedCorners(image);

    }
    public Mat createMosaicWithRoundedCorners(Mat inputMat) {
        Mat mosaicMat = new Mat(inputMat.rows(), inputMat.cols(), CvType.CV_8UC3);

        // Loop through each square in the mosaic
        for (int y = 0; y < inputMat.rows(); y += squareSize) {
            for (int x = 0; x < inputMat.cols(); x += squareSize) {
                // Define the coordinates of the current square
                int x1 = x;
                int y1 = y;
                int x2 = Math.min(x + squareSize, inputMat.cols());
                int y2 = Math.min(y + squareSize, inputMat.rows());

                // Extract the current square from the inputMat
                Mat square = inputMat.submat(y1, y2, x1, x2);

                // Create a mask with rounded corners
                Mat mask = Mat.zeros(square.size(), CvType.CV_8UC1);

                // Adjust the radius to control corner roundness
                int radius = squareSize / radiusRatio;
                rectangle(mask, new Point(0, 0), new Point(square.cols(), square.rows()), new Scalar(255), -1);
                ellipse(mask, new Point(radius, radius), new Size(radius, radius), 180, 0, 90, new Scalar(0), -1);
                ellipse(mask, new Point(square.cols() - radius, radius), new Size(radius, radius), 270, 0, 90, new Scalar(0), -1);
                ellipse(mask, new Point(radius, square.rows() - radius), new Size(radius, radius), 90, 0, 90, new Scalar(0), -1);
                ellipse(mask, new Point(square.cols() - radius, square.rows() - radius), new Size(radius, radius), 0, 0, 90, new Scalar(0), -1);

                // Apply the mask to the square
                square.copyTo(mosaicMat.submat(y1, y2, x1, x2), mask);

                // Apply the mask to the square
                Mat invertedMask = new Mat();
                bitwise_not(mask, invertedMask);
                bitwise_and(square, square, square, invertedMask);

                square.setTo(color, mask);

                // Randomly change the luminosity of the square
                double alpha = 0.5 + Math.random() * 1.5;  // Random alpha value between 0.5 and 2.0
                multiply(square, new Scalar(alpha, alpha, alpha), square);
                square.copyTo(mosaicMat.submat(y1, y2, x1, x2));
            }
        }

        return mosaicMat;
    }

}
