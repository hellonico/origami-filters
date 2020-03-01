package origami.filters.isaac;

import com.isaac.models.RemoveBackScatter;
import org.opencv.core.Mat;
import origami.Filter;

public class RemoveBlackScatter implements Filter {

    int blkSize = 10 * 10;
    int patchSize = 8;
    double lambda = 10;
    double gamma = 1.7;
    int r = 10;
    double eps = 1e-6;
    int level = 5;

    @Override
    public Mat apply(Mat mat) {
        return RemoveBackScatter.enhance(mat, blkSize, patchSize, lambda, gamma, r, eps, level);
    }
}
