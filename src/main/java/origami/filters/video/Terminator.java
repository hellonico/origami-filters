package origami.filters.video;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import origami.Filter;
import origami.filters.video.sub.GridPattern;
import origami.filters.video.sub.RedTint;

import static org.opencv.imgproc.Imgproc.GaussianBlur;

public class Terminator implements Filter {

    public Mat apply(Mat inputImage) {
        Mat out = inputImage.clone();
        inputImage = new RedTint().apply(inputImage);
        GaussianBlur(inputImage, out, new Size(5, 5), 0);
        inputImage = new GridPattern.V2().apply(inputImage);

        return inputImage;
    }

}
