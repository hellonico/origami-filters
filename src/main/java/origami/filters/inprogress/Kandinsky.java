package origami.filters.inprogress;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.imgproc.Imgproc.*;

public class Kandinsky implements Filter {

    @Override
    public Mat apply(Mat mat) {
        Mat inputMat = new Mat();
        cvtColor(mat, inputMat, COLOR_BGR2GRAY);

        GaussianBlur(inputMat, inputMat, new Size(21, 21), 1);

        Mat outputMat = new Mat(inputMat.size(), inputMat.type());
        // Iterate over each pixel in the inputMat
        for (int row = 0; row < inputMat.rows(); row++) {
            for (int col = 0; col < inputMat.cols(); col++) {
                // Get the pixel value at the current position
                double pixelValue = inputMat.get(row, col)[0];

                // Apply Kandinsky filter
                double filteredValue = Math.sin(pixelValue)*pixelValue;

                // Set the filtered value in the outputMat
                outputMat.put(row, col, filteredValue);
            }
        }
        cvtColor(outputMat, outputMat, COLOR_GRAY2BGR);
        return outputMat;
    }
}
