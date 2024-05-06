package origami.filters.video;

import org.opencv.core.Mat;
import origami.Filter;
import origami.filters.video.sub.ScanLines;

public class VHSEnhanced implements Filter {

    public Mat apply(Mat inputImage) {
        inputImage = new VHS().apply(inputImage);

        // Apply scanline effect
        return new ScanLines.Fat().apply(inputImage);
    }

}
