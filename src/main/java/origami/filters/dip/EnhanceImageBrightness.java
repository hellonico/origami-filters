package origami.filters.dip;

import org.opencv.core.Mat;
import origami.Filter;

public class EnhanceImageBrightness implements Filter {

    double alpha = 2;
    double beta = 50;

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    @Override
    public Mat apply(Mat source) {
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        source.convertTo(destination, -1, alpha, beta);
        return destination;
    }
}
