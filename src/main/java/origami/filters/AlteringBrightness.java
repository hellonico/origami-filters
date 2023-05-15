package origami.filters;

import org.opencv.core.Mat;
import origami.Filter;
import origami.annotations.Parameter;
import origami.annotations.Usage;

@Usage(description = "Change brightness of a mat by apply opencv convertTo")
public class AlteringBrightness implements Filter {

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Parameter(description = "Alpha values are use from -1 to 3 are good")
    private double alpha = 1;
    @Parameter(description = "Beta values are from -255 to 255")
    private double beta = 100;

    @Override
    public Mat apply(Mat mat) {
        Mat dest = new Mat();
        mat.convertTo(dest, -1, alpha, beta);
        return dest;
    }
}
