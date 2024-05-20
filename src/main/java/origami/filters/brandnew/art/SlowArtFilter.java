package origami.filters.brandnew.art;

import org.opencv.core.Mat;
import org.opencv.photo.Photo;
import origami.Filter;

import static org.opencv.core.Core.bitwise_and;
import static org.opencv.imgproc.Imgproc.*;

public class SlowArtFilter implements Filter {
    @Override
    public Mat apply(Mat src) {

        // Apply an edge-preserving filter
        Mat edgePreserved = new Mat();
        Photo.edgePreservingFilter(src, edgePreserved, Photo.RECURS_FILTER, 50, 0.4f);

        // Convert to grayscale and apply median blur
        Mat gray = new Mat();
        cvtColor(edgePreserved, gray, COLOR_BGR2GRAY);
        medianBlur(gray, gray, 7);

        // Detect edges using adaptive threshold
        Mat edges = new Mat();
        adaptiveThreshold(gray, edges, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 9, 2);

        // Combine edges with the original image
        Mat coloredEdges = new Mat();
        bitwise_and(edgePreserved, edgePreserved, coloredEdges, edges);

        return coloredEdges;
    }
}
