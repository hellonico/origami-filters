package origami.filters.isaac;

import com.isaac.models.OptimizedContrastEnhance;
import org.opencv.core.Mat;
import origami.Filter;

public class OptimizeContrastEnhance implements Filter {

    int blkSize = 100; // block size
    int patchSize = 4; // patch size
    double lambda = 5.0; // control the relative importance of contrast loss and information loss
    double eps = 1e-8;
    int krnlSize = 10;

    @Override
    public Mat apply(Mat mat) {
        return OptimizedContrastEnhance.enhance(mat, blkSize, patchSize, lambda, eps, krnlSize);
    }
}
