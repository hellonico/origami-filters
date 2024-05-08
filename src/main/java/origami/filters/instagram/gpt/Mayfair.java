package origami.filters.instagram.gpt;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class Mayfair implements Filter {
    @Override
    public Mat apply(Mat image) {
        // Increase brightness and add a pink hue
        Core.addWeighted(image, 1.2, new Mat(image.size(), image.type(), new Scalar(30, 0, 30)), 0.5, 0, image);

        // Enhance shadows
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2Lab);
        Core.add(image, new Mat(image.size(), image.type(), new Scalar(0, 0, -10)), image);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_Lab2BGR);
        return image;
    }
}
