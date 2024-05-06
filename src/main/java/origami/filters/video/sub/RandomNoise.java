package origami.filters.video.sub;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.core.Core.merge;

public class RandomNoise implements Filter {
    public int low = 100;
    public int high = 255;

    public Mat apply(Mat inputImage) {

        List<Mat> noiseChannels = new ArrayList<>();
        split(inputImage, noiseChannels);

        for (Mat channel : noiseChannels) {
            Mat noise = new Mat(inputImage.size(), Imgcodecs.IMREAD_GRAYSCALE);
            randu(noise, low, high);
            add(channel, noise, channel);
        }

        Mat noise = new Mat();
        merge(noiseChannels, noise);

        return noise;
    }

}
