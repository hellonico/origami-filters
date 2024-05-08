package origami.filters.instagram.gpt;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.core.CvType.*;
import static org.opencv.imgproc.Imgproc.*;

public class Lark implements Filter {

    @Override
    public Mat apply(Mat image) {
        // Apply color enhancement by adjusting contrast and saturation
        cvtColor(image, image, COLOR_BGR2Lab);
        multiply(image, new Mat(image.size(), image.type(), new Scalar(1.2, 1.2, 1.2)), image);
        add(image, new Mat(image.size(), image.type(), new Scalar(-10, 0, 0)), image);
        cvtColor(image, image, COLOR_Lab2BGR);

        // Add a vintage effect by reducing the blue channel
        List<Mat> bgrChannels = new ArrayList<Mat>(3);
        split(image, bgrChannels);
        subtract(bgrChannels.get(1), new Mat(image.size(), CV_8UC1, new Scalar(20)), bgrChannels.get(1));
        merge(bgrChannels, image);

        return image;
    }
}
