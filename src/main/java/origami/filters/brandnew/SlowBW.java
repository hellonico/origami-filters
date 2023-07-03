package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class SlowBW {

    public static class V2 implements Filter {

        public boolean leftToRight;

        public boolean isLeftToRight() {
            return leftToRight;
        }

        public void setLeftToRight(boolean leftToRight) {
            this.leftToRight = leftToRight;
        }

        @Override
        public Mat apply(Mat image) {

            // Get the width and height of the image
            int width = image.cols();
            int height = image.rows();

            // Create a gradient matrix for progress effect
            Mat gradient = new Mat(height, width, CvType.CV_32F);
            if(leftToRight) {
                for (int x = 0; x < width; x++) {
                    double progress = (double) x / width;
                    Mat column = gradient.col(x);
                    column.setTo(new Scalar(progress));
                }
            } else {
                for (int x = width - 1; x >= 0; x--) {
                    double progress = (double) (width - x) / width; // Reverse the progress calculation
                    Mat column = gradient.col(x);
                    column.setTo(new Scalar(progress));
                }
            }

            // Convert the image to grayscale
            Mat grayImage = new Mat();
            Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

            // Convert the grayscale image to floating point type
            Mat grayImageFloat = new Mat();
            grayImage.convertTo(grayImageFloat, CvType.CV_32F);

            // Multiply the grayscale image with the gradient matrix
            Mat multiplied = new Mat();
            Core.multiply(grayImageFloat, gradient, multiplied);

            // Convert the multiplied image back to grayscale
            Mat result = new Mat();
            multiplied.convertTo(result, CvType.CV_8U);

            return result;

        }
    }

    public static class V1 implements Filter {
        @Override
        public Mat apply(Mat image) {


            // Get the width and height of the image
            int width = image.cols();
            int height = image.rows();

            // Loop through each column of the image
            for (int x = 0; x < width; x++) {
                // Calculate the progress ratio (0 to 1) based on the current column
                double progress = (double) x / width;

                // Calculate the contrast adjustment based on the progress
                double contrast = 1.0 + 3.0 * progress; // Increase the contrast progressively

                // Loop through each pixel in the current column
                for (int y = 0; y < height; y++) {
                    // Get the current pixel
                    double[] pixel = image.get(y, x);

                    // Convert the pixel to grayscale using weighted RGB channels
                    double grayscale = pixel[0] * 0.2989 + pixel[1] * 0.5870 + pixel[2] * 0.1140;

                    // Adjust the contrast by multiplying the grayscale value
                    double adjustedGrayscale = grayscale * contrast;

                    // Clamp the adjusted grayscale value to the valid range of 0 to 255
                    adjustedGrayscale = Math.max(0, Math.min(255, adjustedGrayscale));

                    // Set the pixel color with the adjusted grayscale value
                    image.put(y, x, adjustedGrayscale, adjustedGrayscale, adjustedGrayscale);
                }
            }

            return image;
        }
    }
}
