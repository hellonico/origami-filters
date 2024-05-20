package origami.filters.video.sub;

import org.opencv.core.Mat;
import origami.Filter;

import java.util.Random;

public class RandomBlue implements Filter {
    Random rand = new Random();

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int blockSize = 20; // Define the size of the blocks

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
                // Generate a random blue enhancement for the block
                int blueShift = rand.nextInt(101) + 50; // Random shift between 50 and 150

                // Apply the blue enhancement to each block
                for (int i = 0; i < blockSize && (row + i) < rows; i++) {
                    for (int j = 0; j < blockSize && (col + j) < cols; j++) {
                        int index = ((row + i) * cols + (col + j)) * channels;
                        buffer[index] = (byte) Math.min((buffer[index] & 0xFF) + blueShift, 255);
                    }
                }
            }
        }

        result.put(0, 0, buffer);
        return result;
    }
}
