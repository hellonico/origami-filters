package origami.filters.brandnew.art;

import org.opencv.core.Mat;
import origami.Filter;

import static org.opencv.core.Core.addWeighted;
import static org.opencv.core.Core.randn;


public class Noisy implements Filter {

    public double ratio1 = 0.4;

    public double noiseMean = 10;
    public double noiseDev = 80;

    public double getRatio1() {
        return ratio1;
    }

    public void setRatio1(double ratio1) {
        this.ratio1 = ratio1;
    }

    public double getNoiseMean() {
        return noiseMean;
    }

    public void setNoiseMean(double noiseMean) {
        this.noiseMean = noiseMean;
    }

    public double getNoiseDev() {
        return noiseDev;
    }

    public void setNoiseDev(double noiseDev) {
        this.noiseDev = noiseDev;
    }

    @Override
    public Mat apply(Mat src) {
        Mat result = src.clone();
        Mat noise = new Mat(src.size(), src.type());
        randn(noise, noiseMean, noiseDev); // Increase the standard deviation to 80 for more significant noise
        addWeighted(src, ratio1, noise, 1 - ratio1, 0, result); // Blend the noise with the original image for a stronger effect
        return result;
    }
}
