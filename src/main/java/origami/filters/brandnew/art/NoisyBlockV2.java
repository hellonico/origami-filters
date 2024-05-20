package origami.filters.brandnew.art;

import org.opencv.core.Mat;
import origami.Filter;

import java.util.Random;

import static org.opencv.core.Core.addWeighted;

public class NoisyBlockV2 implements Filter {
    java.util.Random rand = new Random();

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int blockSize = 20; // Define the size of the blocks

    public double ratio = 0.3;

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public Mat apply(Mat src) {
        Mat result = src.clone();

        // Direct buffer access
        int rows = src.rows();
        int cols = src.cols();
        int channels = src.channels();
        byte[] buffer = new byte[rows * cols * channels];
        result.get(0, 0, buffer);

        for (int row = 0; row < rows; row += blockSize) {
            for (int col = 0; col < cols; col += blockSize) {
                // Generate a random color shift for the block
                int shiftB = rand.nextInt(101) - 50; // Random shift between -50 and 50
                int shiftG = rand.nextInt(101) - 50;
                int shiftR = rand.nextInt(101) - 50;

                // Apply the color shift to each block
                for (int i = 0; i < blockSize && (row + i) < rows; i++) {
                    for (int j = 0; j < blockSize && (col + j) < cols; j++) {
                        int index = ((row + i) * cols + (col + j)) * channels;
                        buffer[index] = (byte) Math.min(Math.max((buffer[index] & 0xFF) + shiftB, 0), 255);
                        buffer[index + 1] = (byte) Math.min(Math.max((buffer[index + 1] & 0xFF) + shiftG, 0), 255);
                        buffer[index + 2] = (byte) Math.min(Math.max((buffer[index + 2] & 0xFF) + shiftR, 0), 255);
                    }
                }
            }
        }

        result.put(0, 0, buffer);

        addWeighted(src, ratio, result, 1 - ratio, 0, result); // Blend the noise with the original image for a stronger effect

        return result;
    }
}
