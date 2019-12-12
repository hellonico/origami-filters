package origami.filters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static org.opencv.core.Core.transform;

/**
 * Using a kernel to get sepia picture
 */
public class Sepia {

    public Mat apply(Mat source) {
        // mat is in BGR
        Mat kernel = new Mat(3, 3, CvType.CV_32F);
        kernel.put(0, 0,
                // green
                0.272, 0.534, 0.131,
                // blue
                0.349, 0.686, 0.168,
                // red
                0.393, 0.769, 0.189);
        Mat destination = new Mat();
        transform(source, destination, kernel);
        return destination;
    }
}