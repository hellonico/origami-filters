package origami.filters.instagram.gpt;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class ShadowEnhance implements Filter {
    @Override
    public Mat apply(Mat image) {
        // Convert the image to LAB color space
        Mat labImage = new Mat();
        cvtColor(image, labImage, COLOR_BGR2Lab);

        // Split the LAB channels
        List<Mat> labChannels = new ArrayList<Mat>(3);
        split(labImage, labChannels);

        // Apply contrast enhancement to the L channel (lightness)
        equalizeHist(labChannels.get(0), labChannels.get(0));
        // Reduce lightness a bit
        multiply(labChannels.get(0), new Scalar(0.9), labChannels.get(0));

        // Merge the LAB channels back
        merge(labChannels, labImage);

        // Convert back to BGR color space
        cvtColor(labImage, image, COLOR_Lab2BGR);

        return image;

    }
}
