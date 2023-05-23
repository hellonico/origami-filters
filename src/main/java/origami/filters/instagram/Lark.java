package origami.filters.instagram;

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
        Mat labImage = new Mat();
        cvtColor(image, labImage, COLOR_BGR2Lab);

        List<Mat> labChannels = new ArrayList<>(3);
        split(labImage, labChannels);
// Adjust the L channel
        Core.add(labChannels.get(0), new Scalar(5), labChannels.get(0));

// Adjust the A channel
        Core.add(labChannels.get(1), new Scalar(-10), labChannels.get(1));

// Adjust the B channel
        Core.add(labChannels.get(2), new Scalar(-5), labChannels.get(2));

        Mat processedLabImage = new Mat();
        merge(labChannels, processedLabImage);

        Mat processedImage = new Mat();
        cvtColor(processedLabImage, processedImage, COLOR_Lab2BGR);

        return processedImage;

    }
}
