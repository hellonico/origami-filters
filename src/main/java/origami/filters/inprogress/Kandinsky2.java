package origami.filters.inprogress;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.imgproc.Imgproc.*;

public class Kandinsky2 implements Filter {

    @Override
    public Mat apply(Mat mat) {
        Mat inputMat = new Mat();
        cvtColor(mat, inputMat, COLOR_RGB2GRAY);
        GaussianBlur(inputMat, inputMat, new Size(21, 21), 1);


        Mat outputMat = new Mat(mat.size(),mat.type());

        Scalar[] colors = new Scalar[]{
                new Scalar(255, 0, 0),    // Red
                new Scalar(0, 255, 0),    // Green
//                new Scalar(0, 0, 255),    // Blue
//                new Scalar(255, 255, 0),  // Yellow
//                new Scalar(0, 255, 255)   // Cyan
        };

        // Iterate over each pixel in the inputMat
        for (int row = 0; row < inputMat.rows(); row++) {
            for (int col = 0; col < inputMat.cols(); col++) {
                // Get the pixel value at the current position
                double pixelValue = inputMat.get(row, col)[0];

                // Calculate the color index based on the pixel value
                int colorIndex = (int) (pixelValue % colors.length);

                // Get the corresponding color from the palette
                Scalar color = colors[colorIndex];

                // Set the color in the outputMat
                outputMat.put(row, col, color.val[2],color.val[1],color.val[0]);
            }
        }

//        cvtColor(outputMat, outputMat, COLOR_GRAY2BGR);

        return outputMat;
    }
}
