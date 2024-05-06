package origami.filters.video.sub;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import origami.Filter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShuffleChannels implements Filter {
    @Override
    public Mat apply(Mat inputImage) {
        List<Mat> temp = new ArrayList<Mat>(3);
        Core.split(inputImage, temp);

        // Shuffle the channels
        List<Mat> temp2 = List.of(temp.get(1), temp.get(2), temp.get(0));
        Core.merge(temp2, inputImage);
        return inputImage;

    }
}
