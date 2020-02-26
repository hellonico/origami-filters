package origami.filters.isaac;

import org.opencv.core.Mat;
import origami.Filter;

public class DarkChannelPriorDehaze implements Filter{
    double krnlRatio = 0.01; // set kernel ratio
    double minAtmosLight = 240.0; // set minimum atmospheric light
    double eps = 0.000001;

    @Override
    public Mat apply(Mat mat) {
        return com.isaac.models.DarkChannelPriorDehaze.enhance(mat, krnlRatio, minAtmosLight, eps);
    }
}
