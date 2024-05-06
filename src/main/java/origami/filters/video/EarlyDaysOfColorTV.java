package origami.filters.video;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import origami.Filter;
import origami.Filters;
import origami.filters.brandnew.AlteringBrightness;
import origami.filters.video.sub.ColorDistortion;
import origami.filters.video.sub.RandomNoise;
import origami.filters.video.sub.ShuffleChannels;

import java.util.List;

import static org.opencv.imgproc.Imgproc.GaussianBlur;

public class EarlyDaysOfColorTV implements Filter {
    Filters fs = new Filters(List.of(new ShuffleChannels(), new RandomNoise(), new AlteringBrightness.Darker(), new ColorDistortion()));

    public Mat apply(Mat inputImage) {
        GaussianBlur(inputImage, inputImage, new Size(5, 5), 0);
        return fs.apply(inputImage);
    }

}
