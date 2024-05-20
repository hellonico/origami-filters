package origami.filters.cartoon;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import origami.Filter;

import static org.opencv.core.Core.bitwise_not;
import static org.opencv.imgproc.Imgproc.*;

public class NewCartoon implements Filter {

    public int lineSize = 17;

    public int getLineSize() {
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }

    @Override
    public Mat apply(Mat src) {


        // Convert the image to grayscale
        Mat gray = new Mat();
        cvtColor(src, gray, COLOR_BGR2GRAY);

        // Apply a median blur to reduce noise
        Mat blurred = new Mat();
        medianBlur(gray, blurred, 7);

        // Detect edges using Canny edge detector
        Mat edges = new Mat();
        Canny(blurred, edges, 50, 150);

        // Dilate the edges to make them thicker
        Mat dilatedEdges = new Mat();
        Mat kernel = getStructuringElement(MORPH_RECT, new Size(lineSize, lineSize));  // Increase kernel size to 5x5
        dilate(edges, dilatedEdges, kernel);

        // Invert the edges to get a black and white image
        Mat invertedEdges = new Mat();
        bitwise_not(dilatedEdges, invertedEdges);

        // Apply a binary threshold to get a binary image
        Mat bwImage = new Mat();
        threshold(invertedEdges, bwImage, 100, 255, THRESH_BINARY);

        return bwImage;
    }
}
