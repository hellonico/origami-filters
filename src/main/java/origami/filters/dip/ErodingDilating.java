package origami.filters.dip;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class ErodingDilating implements Filter {

    @Override
    public Mat apply(Mat source) {
        Mat destination = new Mat();

        int erosion_size = 5;
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(2 * erosion_size + 1, 2 * erosion_size + 1));
        Imgproc.erode(source, destination, element);
        Imgcodecs.imwrite("erosion.jpg", destination);

        int dilation_size = 5;
        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(2 * dilation_size + 1, 2 * dilation_size + 1));
        Imgproc.dilate(source, destination, element1);
        return destination;
    }
}
