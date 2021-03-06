package origami.filters;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class Gray implements Filter {

    public Mat apply(Mat img) {
        Mat mat1 = new Mat();
        Imgproc.cvtColor(img, mat1, Imgproc.COLOR_RGB2GRAY);
        return mat1;
    }

}
