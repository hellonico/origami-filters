package origami.filters.video.sub;

import org.opencv.core.Mat;
import origami.Filter;

public class RedTint implements Filter {
    @Override
    public Mat apply(Mat inputImage) {
        for (int y = 0; y < inputImage.rows(); y++) {
            for (int x = 0; x < inputImage.cols(); x++) {
                double[] pixel = inputImage.get(y, x);
                pixel[0] = 0; // Set blue channel to 0
                pixel[1] = 0; // Set green channel to 0
                inputImage.put(y, x, pixel);
            }
        }
        return inputImage;
    }
}
