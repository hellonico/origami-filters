package origami.filters;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.bitwise_not;
import static org.opencv.core.Core.bitwise_or;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgproc.Imgproc.COLOR_GRAY2RGB;
import origami.Filter;

public class Canny implements Filter {
    public boolean inverted = true;
    public int threshold1 = 100;
    public int threshold2 = 200;

    public int getThreshold1() {
        return threshold1;
    }

    public void setThreshold1(int threshold1) {
        this.threshold1 = threshold1;
    }

    public int getThreshold2() {
        return threshold2;
    }

    public void setThreshold2(int threshold2) {
        this.threshold2 = threshold2;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public Mat apply(Mat in) {
        Mat dst = new Mat();
        Canny(in, dst, threshold1, threshold2);
        if (inverted) {
            bitwise_not(dst, dst, new Mat());
        }
        cvtColor(dst, dst, COLOR_GRAY2RGB);
        return dst;
    }
}
