package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.imgproc.Imgproc.resize;

public class Mosaic implements Filter {
    @Override
    public Mat apply(Mat in) {
        Mat im = in.clone();
        resize(im, im, new Size(), 0.1, 0.1, Imgproc.INTER_NEAREST);
        resize(im, im, new Size(), 10.0, 10.0, Imgproc.INTER_NEAREST);
        return im;
    }
}
