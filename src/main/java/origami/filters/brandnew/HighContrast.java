package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class HighContrast implements Filter {
    public double alpha = 1.5;

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public Mat apply(Mat image) {
        Mat labImage = new Mat(), processedImage = new Mat();
        cvtColor(image, labImage, COLOR_BGR2HSV);

        List<Mat> labChannels = new ArrayList<>();
        split(labImage, labChannels);

        equalizeHist(labChannels.get(2), labChannels.get(2));

        multiply(labChannels.get(1), new Scalar(alpha), labChannels.get(1));
        subtract(labChannels.get(2), new Scalar(10), labChannels.get(2));

        merge(labChannels, processedImage);

        cvtColor(processedImage, processedImage, COLOR_HSV2BGR);

        return processedImage;
    }
}
