package origami.filters.instagram.gpt;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import origami.Filter;

import static org.opencv.core.Core.*;

public class XProII implements Filter {
    @Override
    public Mat apply(Mat image) {
        // Increase contrast and saturation
        multiply(image, new Mat(image.size(), image.type(), new Scalar(1.2, 1.2, 1.2)), image);
        add(image, new Mat(image.size(), image.type(), new Scalar(-20, -20, -20)), image);

        // Add a warm, vintage tone
        addWeighted(image, 0.8, new Mat(image.size(), image.type(), new Scalar(0, 50, 100)), 0.2, 0, image);
        return image;
    }
}
