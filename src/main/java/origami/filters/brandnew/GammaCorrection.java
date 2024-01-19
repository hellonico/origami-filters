package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import origami.Filter;
import origami.annotations.Usage;

@Usage(description = "Gamma Correction")
public class GammaCorrection implements Filter {
    float gamma = 0.0f;

    public float getGamma() {
        return gamma;
    }

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    public void gammaCorrection(Mat src, Mat dst, float gamma)
    {
        float invGamma = 1 / gamma;

        Mat table = new Mat(1, 256, CvType.CV_8U);
        for (int i = 0; i < 256; ++i) {
            table.put(0, i, (int) (Math.pow(i / 255.0f, invGamma) * 255));
        }

        Core.LUT(src, table, dst);
    }
    @Override
    public Mat apply(Mat img) {
        Mat gammaImg = new Mat(img.size(),img.type());
        gammaCorrection(img, gammaImg, gamma);
        return gammaImg;
    }
}
