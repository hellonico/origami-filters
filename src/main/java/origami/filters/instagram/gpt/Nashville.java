package origami.filters.instagram.gpt;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class Nashville implements Filter {
    @Override
    public Mat apply(Mat image) {
        // Apply warm vintage tone
        addWeighted(image, 0.9, new Mat(image.size(), image.type(), new Scalar(0, 30, 50)), 0.1, 0, image);

        // Enhance shadows
        cvtColor(image, image, COLOR_BGR2Lab);
        add(image, new Mat(image.size(), image.type(), new Scalar(0, 0, -10)), image);
        cvtColor(image, image, COLOR_Lab2BGR);

        // Add pinkish hue
        addWeighted(image, 0.7, new Mat(image.size(), image.type(), new Scalar(0, 0, 50)), 0.3, 0, image);
        return image;
    }
}
