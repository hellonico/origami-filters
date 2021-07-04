package origami.filters.dip;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.imgproc.Imgproc.*;

public class EnhanceImageContrast implements Filter {

    @Override
    public Mat apply(Mat source) {
        Mat destination = new Mat();
        cvtColor(source, destination, COLOR_RGB2GRAY);
        equalizeHist(destination, destination);
        return destination;
    }
}
