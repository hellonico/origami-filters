package origami.filters.inprogress;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class Fake3D {

    public static class V3 implements Filter {

        @Override
        public Mat apply(Mat inputImage) {

            // Split the image into color channels
            List<Mat> channels = new ArrayList<Mat>(3);
            split(inputImage, channels);

            // Apply Gaussian blur to each color channel
            for (int i = 0; i < 3; i++) {
                Mat blurredChannel = new Mat();
                Imgproc.GaussianBlur(channels.get(i), blurredChannel, new Size(0, 0), 3);

                // Subtract the blurred channel from the original channel to enhance details
                subtract(channels.get(i), blurredChannel, channels.get(i));
            }

            // Merge the enhanced color channels
            Mat outputImage = new Mat();
            merge(channels, outputImage);

            return outputImage;

        }
    }

    public static class V2 implements Filter {

        @Override
        public Mat apply(Mat inputImage) {

            // Split the image into color channels
            //Mat[] channels = new Mat[3];
            List<Mat> channels = new ArrayList<Mat>(3);
            split(inputImage, channels);

            // Apply bilateral filtering to each color channel
            for (int i = 0; i < 3; i++) {
                Mat filteredChannel = new Mat();
                bilateralFilter(channels.get(i), filteredChannel, 9, 75, 75, BORDER_DEFAULT);
                subtract(channels.get(i), filteredChannel, channels.get(i));
            }

            // Merge the enhanced color channels
            Mat outputImage = new Mat();
            merge(channels, outputImage);

            // Increase the brightness of the output image
            add(outputImage, new Scalar(50, 50, 50), outputImage); // Adjust the scalar values as needed

            return outputImage;

        }
    }

    public static class V1 implements Filter {
        @Override
        public Mat apply(Mat inputImage) {

            // Convert the image to grayscale
            Mat grayImage = new Mat();
            cvtColor(inputImage, grayImage, COLOR_BGR2GRAY);

            // Convert grayscale image to float type
            Mat grayFloat = new Mat();
            grayImage.convertTo(grayFloat, CvType.CV_32F);

            // Apply bilateral filtering to create the 3D effect
            Mat filteredImage = new Mat();
            bilateralFilter(grayFloat, filteredImage, 9, 75, 75, BORDER_DEFAULT);

            // Convert the filtered image back to 8-bit grayscale
            Mat filteredGray = new Mat();
            filteredImage.convertTo(filteredGray, CvType.CV_8U);

            // Subtract the filtered image from the grayscale image to make the main parts stand out
            Mat outputImage = new Mat();
            subtract(grayImage, filteredGray, outputImage);


            // Increase the brightness of the output image
            add(outputImage, new Scalar(brightness), outputImage); // Adjust the scalar value as needed

            // Increase the contrast of the output image
            multiply(outputImage, new Scalar(contrast), outputImage); // Adjust the scalar value as needed



            return outputImage;

        }

        public double contrast = 1.5;

        public double getContrast() {
            return contrast;
        }

        public void setContrast(double contrast) {
            this.contrast = contrast;
        }

        public int brightness = 50;

        public int getBrightness() {
            return brightness;
        }

        public void setBrightness(int brightness) {
            this.brightness = brightness;
        }
    }

}
