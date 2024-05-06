package origami.filters.video;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.Filters;
import origami.filters.video.sub.RandomNoise;
import origami.filters.video.sub.Scratches;
import origami.filters.video.sub.Vignetting;

import java.util.List;
import java.util.Random;

public class OldFilm implements Filter {

    public Mat apply(Mat inputImage) {
        // Apply Gaussian blur
        Imgproc.GaussianBlur(inputImage, inputImage, new Size(15, 15), 0);
        Filters fs = new Filters();
        fs.setFilters(List.of(new RandomNoise(), new Scratches(), new Vignetting()));
        return fs.apply(inputImage);
    }

}
