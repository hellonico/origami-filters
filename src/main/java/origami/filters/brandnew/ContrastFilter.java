package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import origami.Filter;
import origami.annotations.Parameter;
import origami.annotations.Usage;

@Usage(description = "Change the contrast with alpha and beta blending")
public class ContrastFilter implements Filter {

    @Parameter(description = "Default value of 1.5")
    private double alpha = 1.5;
    @Parameter(description = "Default value of 10")
    private double beta = 10;

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
    public Mat apply(Mat mat) {
        Mat dst = new Mat();
        Core.convertScaleAbs(mat, dst, alpha, beta);
        return dst;
    }
}
