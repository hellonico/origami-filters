package origami.filters;

import org.opencv.core.Mat;

import static org.opencv.photo.Photo.detailEnhance;
import origami.Filter;

public class DetailEnhance implements Filter {
    public float sigma_s = 10;
    public float sigma_r = 0.15f;

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
    public Mat apply(Mat src) {

        Mat dst = new Mat();
        detailEnhance(src, dst, sigma_s, sigma_r);
        return dst;
    }
}
