package origami.filters.video.sub;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

public class ColorSeparation implements Filter {
    @Override
    public Mat apply(Mat inputImage) {
        List<Mat> channels = new ArrayList<>();
        Core.split(inputImage, channels);

        for (int i = 0; i < channels.size(); i++) {
            Mat channel = channels.get(i);
            for (int j = 0; j < channels.size(); j++) {
                if (j != i) {
                    channels.get(j).setTo(new Scalar(0)); // Set other channels to zero
                }
            }
            Core.merge(channels, inputImage);
        }
        return inputImage;
    }
}
