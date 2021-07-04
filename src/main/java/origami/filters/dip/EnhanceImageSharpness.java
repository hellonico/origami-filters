package origami.filters.dip;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class EnhanceImageSharpness implements Filter {

    @Override
    public Mat apply(Mat source) {
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.GaussianBlur(source, destination, new Size(1, 1), 10);
        Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);
        return destination;
    }
}
