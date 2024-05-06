package origami.filters.video;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import origami.Filter;
import origami.filters.brandnew.AlteringBrightness;
import origami.filters.video.sub.ColorDistortion;
import origami.filters.video.sub.RandomNoise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgproc.Imgproc.GaussianBlur;

public class VHS implements Filter {

    public Mat apply(Mat inputImage) {
        Mat out = new Mat(inputImage.size(), inputImage.type());
        // Apply Gaussian blur
        blur(inputImage, out, new Size(35, 35));

        out = new ColorDistortion().apply(out);
        out = new RandomNoise().apply(out);
        out = new AlteringBrightness.Darker().apply(out);
        return out;

    }

}
