package origami.filters.video.sub;

import org.opencv.core.Mat;
import origami.Filter;

import java.util.Random;

public class ScanLines {
    public static class Simple implements Filter {

        @Override
        public Mat apply(Mat inputImage) {
            Random rand = new Random();
            int intensity = 50; // Adjust the intensity of scanlines

            for (int y = 0; y < inputImage.rows(); y++) {
                if (rand.nextBoolean()) {
                    for (int x = 0; x < inputImage.cols(); x++) {
                        double[] pixel = inputImage.get(y, x);
                        for (int c = 0; c < inputImage.channels(); c++) {
                            pixel[c] = Math.max(0, pixel[c] - intensity); // Reduce pixel intensity
                        }
                        inputImage.put(y, x, pixel);
                    }
                }
            }
            return inputImage;
        }
    }

    public static class Fat implements Filter {
        public int maxLines = 20; // Maximum number of lines to affect
        public int maxLineThickness = 5; // Maximum thickness of lines


        @Override
        public Mat apply(Mat inputImage) {
            Random rand = new Random();

            int numLines = rand.nextInt(maxLines) + 1; // Randomly select number of lines to affect
            for (int i = 0; i < numLines; i++) {
                int y = rand.nextInt(inputImage.rows()); // Randomly select a row
                int lineThickness = rand.nextInt(maxLineThickness) + 1; // Randomly select line thickness

                for (int j = 0; j < inputImage.cols(); j++) {
                    for (int k = 0; k < inputImage.channels(); k++) {
                        double[] pixel = inputImage.get(y, j);
                        pixel[k] = 255; // Set pixel to maximum intensity
                        inputImage.put(y, j, pixel);
                    }
                }

                // Increase line thickness
                for (int k = 1; k <= lineThickness; k++) {
                    if (y + k < inputImage.rows()) {
                        for (int j = 0; j < inputImage.cols(); j++) {
                            for (int c = 0; c < inputImage.channels(); c++) {
                                double[] pixel = inputImage.get(y + k, j);
                                pixel[c] = 255; // Set pixel to maximum intensity
                                inputImage.put(y + k, j, pixel);
                            }
                        }
                    }
                }
            }
            return inputImage;
        }
    }
}
