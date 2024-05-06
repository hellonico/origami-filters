package origami.filters.video.sub;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import origami.Filter;

import java.util.Random;

import static org.opencv.imgproc.Imgproc.*;

public class GridPattern {
    public static class V2 implements Filter {
        Random rand = new Random();

        public int spacing = 45; // Spacing between grid lines

        public int thickness = 3; // Thickness of grid lines


        public int getSpacing() {
            return spacing;
        }

        public void setSpacing(int spacing) {
            this.spacing = spacing;
        }

        public int getThickness() {
            return thickness;
        }

        public void setThickness(int thickness) {
            this.thickness = thickness;
        }


        @Override
        public Mat apply(Mat inputImage) {

            for (int y = 0; y < inputImage.rows(); y += spacing) {
                int thickness = (rand.nextInt(3) + 1) * this.thickness; // Randomly vary line thickness
                line(inputImage, new Point(0, y), new Point(inputImage.cols() - 1, y), new Scalar(0, 0, 255), thickness);
            }

            for (int x = 0; x < inputImage.cols(); x += spacing) {
                int thickness = (rand.nextInt(3) + 1) * this.thickness; // Randomly vary line thickness
                line(inputImage, new Point(x, 0), new Point(x, inputImage.rows() - 1), new Scalar(0, 0, 255), thickness);
            }
            return inputImage;
        }
    }

    public static class V1 implements Filter {
        public int size = 40; // Adjust the size of the grid
        public double probability = 0.02;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public double getProbability() {
            return probability;
        }

        public void setProbability(double probability) {
            this.probability = probability;
        }

        @Override
        public Mat apply(Mat inputImage) {
            Random rand = new Random();

            for (int y = 0; y < inputImage.rows(); y += size) {
                for (int x = 0; x < inputImage.cols(); x += size) {
                    if (rand.nextDouble() < probability) { // Adjust the probability of showing the grid
                        for (int i = y; i < Math.min(y + size, inputImage.rows()); i++) {
                            for (int j = x; j < Math.min(x + size, inputImage.cols()); j++) {
                                double[] pixel = inputImage.get(i, j);
                                pixel[0] = 255; // Set blue channel to maximum
                                pixel[1] = 255; // Set green channel to maximum
                                pixel[2] = 255; // Set red channel to maximum
                                inputImage.put(i, j, pixel);
                            }
                        }
                    }
                }
            }
            return inputImage;
        }
    }

}