package origami.filters.isaac;

import org.opencv.core.Mat;
import origami.Filter;

public class FusionEnhance implements Filter {
    int level = 5;

    @Override
    public Mat apply(Mat mat) {
        return com.isaac.models.FusionEnhance.enhance(mat, level);
    }
}
