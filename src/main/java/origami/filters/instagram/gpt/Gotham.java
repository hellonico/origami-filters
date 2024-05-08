package origami.filters.instagram.gpt;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class Gotham implements Filter {

    public Mat apply(Mat image) {

        // Convert the image to LAB color space
        Mat labImage = new Mat();
        cvtColor(image, labImage, COLOR_BGR2Lab);

        // Adjust contrast and brightness (L channel)
        multiply(labImage, new Scalar(1.2, 1.2, 1.2), labImage);
        add(labImage, new Scalar(-10, -10, -10), labImage);

        // Apply blue tint (B channel)
        List<Mat> bgrChannels = new ArrayList<Mat>(3);
        split(labImage, bgrChannels);
        bgrChannels = List.of(bgrChannels.get(0), bgrChannels.get(1), new Mat(image.size(), 0, new Scalar(100)));
        merge(bgrChannels, labImage);

        // Convert back to BGR color space
        Mat output = new Mat();
        cvtColor(labImage, output, COLOR_Lab2BGR);

        return output;
    }
}
