package origami.filters.inprogress;

import org.opencv.core.*;
import origami.Filter;

public class SquareSplitter implements Filter {
    @Override
    public Mat apply(Mat inputImage) {

        // Determine the size of each small square
        int squareSize = 100;

        // Determine the number of rows and columns of small squares
        int numRows = inputImage.rows() / squareSize;
        int numCols = inputImage.cols() / squareSize;

        // Create a new output image with the same dimensions as the input image
        Mat outputImage = new Mat(inputImage.rows(), inputImage.cols(), CvType.CV_8UC3);

        // Loop through each small square
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                // Define the region of interest (ROI) for the current small square
                Rect roi = new Rect(col * squareSize, row * squareSize, squareSize, squareSize);

                // Extract the current small square from the input image
                Mat smallSquare = new Mat(inputImage, roi);

                // Determine the average color of the current small square
                Scalar averageColor = Core.mean(smallSquare);

                // Replace the current small square in the output image with the nearest color square
                for (int i = roi.y; i < roi.y + squareSize; i++) {
                    for (int j = roi.x; j < roi.x + squareSize; j++) {
                        outputImage.put(i, j, averageColor.val);
                    }
                }
            }
        }

        return outputImage;
    }
}
