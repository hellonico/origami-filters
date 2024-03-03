package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.colors.HTML;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class RedBlack2 implements Filter {

    @Override
    public Mat apply(Mat image) {

        // Convert the image to grayscale
        Mat grayImage = new Mat();
        cvtColor(image, grayImage, COLOR_BGR2GRAY);

        // Apply binary thresholding to convert the grayscale image to black and white
        Mat binaryImage = new Mat();
        double threshold = threshold(grayImage, binaryImage, 0, 255, THRESH_BINARY | THRESH_OTSU);

        // Find contours in the binary image
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(binaryImage, contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

        // Create a black image (Mat) of the same size as the original image
        Mat outputImage = new Mat(image.rows(), image.cols(), CvType.CV_8UC3, Scalar.all(0));

        // Draw the main shapes in sharp red
        drawMainShapes(outputImage, contours);

        return outputImage;

    }

    private static void drawMainShapes(Mat image, List<MatOfPoint> contours) {
        for (MatOfPoint contour : contours) {
            // Calculate the contour area
            double area = contourArea(contour);

            // Set the color based on the contour area
            Scalar color = (area > 1000) ? new Scalar(0, 0, 255) : new Scalar(0, 0, 0);

            // Draw the contour on the image
            drawContours(image, contours, contours.indexOf(contour), color, -1);
        }
    }

}