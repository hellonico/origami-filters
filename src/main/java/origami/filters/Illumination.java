package origami.filters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import static org.opencv.photo.Photo.illuminationChange;
import origami.Filter;

public class Illumination implements Filter {
    public float alpha = 3f;
    public float beta = 0.4f;

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getBeta() {
        return beta;
    }

    public void setBeta(float beta) {
        this.beta = beta;
    }

    @Override
    public Mat apply(Mat in) {
        Mat dst = new Mat();
        Mat mask = Mat.ones(in.size(), CvType.CV_8UC1);
        illuminationChange(in, mask, dst, alpha, beta);
        return dst;
    }
}
