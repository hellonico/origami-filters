package origami.filters.dip;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.utils.Utils;

public class GaussianFilter implements Filter {

    Size size = new Size(11, 11);
    int sigmaX = 0;

    public String getSize() {
        return Utils.Size_String(this.size);
    }

    public void setSize(String size) {
        this.size = Utils.String_Size(size);
    }

    public int getSigmaX() {
        return sigmaX;
    }

    public void setSigmaX(int sigmaX) {
        this.sigmaX = sigmaX;
    }

    @Override
    public Mat apply(Mat source) {
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        Imgproc.GaussianBlur(source, destination, size, sigmaX);
        return destination;
    }
}
