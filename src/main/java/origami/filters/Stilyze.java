package origami.filters;

import org.opencv.core.Mat;

import static org.opencv.photo.Photo.stylization;
import origami.Filter;

public class Stilyze implements Filter {
    float sigma_s = 60;
    // float sigma_r=45;
    float sigma_r = 0.07f;

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
        stylization(in, dst, sigma_s, sigma_r);
        return dst;
    }
}
