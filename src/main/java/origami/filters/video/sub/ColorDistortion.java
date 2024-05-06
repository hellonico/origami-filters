package origami.filters.video.sub;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class ColorDistortion implements Filter {
    public double hueShift = -50;
    public double saturationScale = 2.0;
    public double valueScale = 1.1;

//    public double brightnessScale = 1.2;

    public Mat apply(Mat inputImage) {
        // Convert image from BGR to HSV color space
        Mat hsvImage = new Mat();
        cvtColor(inputImage, hsvImage, COLOR_BGR2HSV);

        // Split HSV channels
        List<Mat> channels = new ArrayList<Mat>(3);
        Core.split(hsvImage, channels);

        // Adjust hue
        channels.get(0).convertTo(channels.get(0), -1, hueShift, 0);

        // Scale saturation
        channels.get(1).convertTo(channels.get(1), -1, saturationScale, 0);

        // Scale value
        channels.get(2).convertTo(channels.get(2), -1, valueScale, 0);

        // Scale brightness
//        Core.addWeighted(channels.get(2), brightnessScale, new Mat(channels.get(2).size(), 0), 0, 0, channels.get(2));


//        Grayscale
        // Merge channels back into HSV image
        Core.merge(channels, hsvImage);

        // Convert HSV image back to BGR color space
        Mat outputImage = new Mat();
        cvtColor(hsvImage, outputImage, COLOR_HSV2BGR);

        return outputImage;
    }
}
