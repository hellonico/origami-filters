package origami.filters.brandnew;

import org.opencv.core.Mat;
import origami.Filter;
import origami.annotations.Usage;

@Usage(description = "Changing the contrast and brightness of an image")
public class BasicLinear implements Filter {
    double alpha = 2.0; /*< Simple contrast control */
    int beta = 0;       /*< Simple brightness control */

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    /**
     * https://docs.opencv.org/3.4/d3/dc1/tutorial_basic_linear_transform.html
     */

    private byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (Math.max(iVal, 0));
        return (byte) iVal;
    }

    @Override
    public Mat apply(Mat image) {

        Mat newImage = new Mat(image.size(),image.type());
        byte[] imageData = new byte[(int) (image.total()*image.channels())];
        image.get(0, 0, imageData);
        byte[] newImageData = new byte[(int) (newImage.total()*newImage.channels())];
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                for (int c = 0; c < image.channels(); c++) {
                    double pixelValue = imageData[(y * image.cols() + x) * image.channels() + c];
                    pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
                    newImageData[(y * image.cols() + x) * image.channels() + c]
                            = saturate(alpha * pixelValue + beta);
                }
            }
        }
        newImage.put(0, 0, newImageData);
        return newImage;
    }
}
