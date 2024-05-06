package origami.filters.video.sub;

import org.opencv.core.Mat;
import origami.Filter;

public class Vignetting implements Filter {
    @Override
    public Mat apply(Mat inputImage) {
        int radius = Math.min(inputImage.width(), inputImage.height()) / 2;
        double strength = 0.5; // Adjust the strength of vignetting

        for (int y = 0; y < inputImage.rows(); y++) {
            for (int x = 0; x < inputImage.cols(); x++) {
                double[] pixel = inputImage.get(y, x);
                double distance = Math.sqrt(Math.pow(x - inputImage.cols() / 2, 2) + Math.pow(y - inputImage.rows() / 2, 2));
                double vignette = 1 - Math.min(1, distance / radius) * strength;
                for (int c = 0; c < inputImage.channels(); c++) {
                    pixel[c] *= vignette;
                }
                inputImage.put(y, x, pixel);
            }
        }
        return inputImage;
    }

}
