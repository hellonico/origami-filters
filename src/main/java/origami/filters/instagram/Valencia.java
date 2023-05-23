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

public class Valencia implements Filter {
    @Override
    public Mat apply(Mat image) {
        Mat hsvImage = new Mat();
        cvtColor(image, hsvImage, COLOR_BGR2HSV);

        List<Mat> hsvChannels = new ArrayList<>(3);
        split(hsvImage, hsvChannels);

        // Increase the saturation
        add(hsvChannels.get(1), new Scalar(30), hsvChannels.get(1));


        // Decrease the value/brightness
        add(hsvChannels.get(2), new Scalar(-30), hsvChannels.get(2));

        // Increase the hue slightly
        add(hsvChannels.get(0), new Scalar(10), hsvChannels.get(0));
        Core.normalize(hsvChannels.get(0),hsvChannels.get(0), 150,255);

        Mat processedHsvImage = new Mat();
        merge(hsvChannels, processedHsvImage);

        Mat processedImage = new Mat();
        cvtColor(processedHsvImage, processedImage, COLOR_HSV2BGR);

        return processedImage;

    }
}
