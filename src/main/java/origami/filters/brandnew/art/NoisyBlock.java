package origami.filters.brandnew.art;

import org.opencv.core.Mat;
import origami.Filter;

import java.util.Random;

public class NoisyBlock implements Filter {
    java.util.Random rand = new Random();
    public int blockSize = 20; // Define the size of the blocks

    @Override
    public Mat apply(Mat src) {
        Mat result = src.clone();

        for (int row = 0; row < src.rows(); row += blockSize) {
            for (int col = 0; col < src.cols(); col += blockSize) {
                // Generate a random color shift for the block
                int shiftB = rand.nextInt(101) - 50; // Random shift between -50 and 50
                int shiftG = rand.nextInt(101) - 50;
                int shiftR = rand.nextInt(101) - 50;

                // Apply the color shift to each pixel in the block
                for (int i = 0; i < blockSize && (row + i) < src.rows(); i++) {
                    for (int j = 0; j < blockSize && (col + j) < src.cols(); j++) {
                        double[] pixel = src.get(row + i, col + j);
                        pixel[0] = Math.min(Math.max(pixel[0] + shiftB, 0), 255);
                        pixel[1] = Math.min(Math.max(pixel[1] + shiftG, 0), 255);
                        pixel[2] = Math.min(Math.max(pixel[2] + shiftR, 0), 255);
                        result.put(row + i, col + j, pixel);
                    }
                }
            }
        }

        return result;
    }
}
