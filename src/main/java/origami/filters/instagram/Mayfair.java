package origami.filters.instagram;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class Mayfair implements Filter {
    @Override
    public Mat apply(Mat image) {
        Mat hsvImage = new Mat();
        cvtColor(image, hsvImage, COLOR_BGR2HSV);

        List<Mat> hsvChannels = new ArrayList<>(3);
        split(hsvImage, hsvChannels);

        // Adjust the hue
        add(hsvChannels.get(0), new Scalar(10), hsvChannels.get(0));

        // Adjust the saturation
        multiply(hsvChannels.get(1), new Scalar(0.8), hsvChannels.get(1));

        // Adjust the value/brightness
        multiply(hsvChannels.get(2), new Scalar(1.2), hsvChannels.get(2));

        Mat processedHsvImage = new Mat();
        merge(hsvChannels, processedHsvImage);

        Mat processedImage = new Mat();
        cvtColor(processedHsvImage, processedImage, COLOR_HSV2BGR);

        double alpha = 1.1; // Contrast adjustment factor
        double beta = 10; // Brightness adjustment factor

        processedImage.convertTo(processedImage, -1, alpha, beta); // Apply contrast and brightness adjustments

        // Apply saturation adjustment
        Mat hsvImage2 = new Mat();
        cvtColor(processedImage, hsvImage2, COLOR_BGR2HSV);

        hsvChannels = new ArrayList<>(3);
        split(hsvImage2, hsvChannels);
        multiply(hsvChannels.get(1), new Scalar(1.7), hsvChannels.get(1));
        merge(hsvChannels, hsvImage2);

        cvtColor(hsvImage2, processedImage, COLOR_HSV2BGR);

        return processedImage;

    }
}
