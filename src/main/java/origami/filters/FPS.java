package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import origami.Filter;
import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_PLAIN;
import static org.opencv.imgproc.Imgproc.putText;

public class FPS extends Annotate implements Filter {

    long start = System.currentTimeMillis();
    int count = 0;

    @Override
    public Mat apply(Mat in) {
        count++;
        if(count > 10) {
            long end = System.currentTimeMillis();
            long fps = (end - start ) / 10000;
            setText("FPS: "+fps);
            // reset
            start = end;
            count = 0;
        }
        return super.apply(in);
    }
}
