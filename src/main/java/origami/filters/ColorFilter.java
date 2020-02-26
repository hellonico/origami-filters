package origami.filters;

import static org.opencv.core.Core.bitwise_and;
import static org.opencv.core.Core.inRange;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.cvtColor;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import origami.Filter;

import java.awt.*;

public class ColorFilter implements Filter {
    static public class Red extends ColorFilter {
        public Red() {
            super(COLOR.RED);
        }
    }
    static public class Blue extends ColorFilter {
        public Blue() {
            super(COLOR.BLUE);
        }
    }
    static public class Pink extends ColorFilter {
        public Pink() {
            super(COLOR.PINK);
        }
    }

    enum COLOR {
        RED, BLUE, PINK
    }

    private int low = 0;
    private int high = 20;

    /**
     * https://i.stack.imgur.com/SobpV.jpg divided by 2
     * https://stackoverflow.com/questions/17878254/opencv-python-cant-detect-blue-objects
     */
    public ColorFilter(int low, int high) {
        this.low = low;
        this.high = high;
    }

    public ColorFilter(COLOR c) {
        switch (c) {
        case RED:
            this.low = 0;
            this.high = 20;
            break;
        case BLUE:
            this.low = 200;
            this.high = 240;
            break;
        case PINK:
            this.low = 300;
            this.high = 320;
            break;
        default:
            ;
        }
    }

    public Mat apply(Mat frame) {
        Mat result = new Mat();
        Mat hsv = new Mat();
        cvtColor(frame, hsv, COLOR_BGR2HSV);
        Mat mask = new Mat();
        Scalar lower = new Scalar(low >> 1, 100, 0);
        Scalar upper = new Scalar(high >> 1, 255, 255);
        inRange(hsv, lower, upper, mask);
        bitwise_and(frame, frame, result, mask);
        return result;
    }

}
