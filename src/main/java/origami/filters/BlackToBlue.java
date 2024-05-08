package origami.filters;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.Arrays;

import static origami.colors.HTML.toScalar;

public class BlackToBlue implements Filter {

    public String color = "#6488ea";
    public double threshold = 50.0;

    public Mat apply(Mat image) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        double[] value = Arrays.copyOfRange(toScalar(color).val,0,3);

        // Iterate through each pixel
        for (int y = 0; y < grayImage.rows(); y++) {
            for (int x = 0; x < grayImage.cols(); x++) {
                if (grayImage.get(y, x)[0] <= threshold) {
                    image.put(y, x, value);
                }
            }
        }

        return image;
    }
}
