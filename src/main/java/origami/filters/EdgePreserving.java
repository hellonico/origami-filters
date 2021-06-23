package origami.filters;

import org.opencv.core.Mat;
import origami.Filter;

import static org.opencv.photo.Photo.*;

public class EdgePreserving implements Filter {
    public int flags = RECURS_FILTER;
//    int flags = NORMCONV_FILTER;
    public float sigma_s = 60;
    public float sigma_r = 0.4f;

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public float getSigma_s() {
        return sigma_s;
    }

    public void setSigma_s(float sigma_s) {
        this.sigma_s = sigma_s;
    }

    public float getSigma_r() {
        return sigma_r;
    }

    public void setSigma_r(float sigma_r) {
        this.sigma_r = sigma_r;
    }

    @Override
    public Mat apply(Mat in) {
        Mat dst = new Mat();
        edgePreservingFilter(in, dst, flags, sigma_s, sigma_r);
        return dst;
    }
}
