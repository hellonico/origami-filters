package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

public class Manga implements Filter {

    @Override
    public Mat apply(Mat mat) {
        Imgproc.cvtColor(mat,mat,COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(mat, mat, new Size(7, 7), 1.5, 1.5);
        Imgproc.threshold(mat,mat,100,255,THRESH_BINARY);
        return mat;
    }
}
