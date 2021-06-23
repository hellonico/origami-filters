package origami.filters;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import origami.Filter;

import static org.opencv.core.Core.*;

public abstract class Rotate implements Filter {
    int rotateAngle = ROTATE_90_CLOCKWISE;

    public int getRotateAngle() {
        return rotateAngle;
    }

    public void setRotateAngle(int rotateAngle) {
        this.rotateAngle = rotateAngle;
    }

    public static class Rotate90 extends Rotate {
        public Rotate90(){
            super();
            this.setRotateAngle(ROTATE_90_CLOCKWISE);
        }
    }
    public static class Rotate180 extends Rotate {
        public Rotate180(){
            super();
            this.setRotateAngle(ROTATE_180);
        }
    }
    public static class Rotate270 extends Rotate {
        public Rotate270(){
            super();
            this.setRotateAngle(ROTATE_90_COUNTERCLOCKWISE);
        }
    }

    public Mat apply(Mat in) {
        Mat r = new Mat();
        Core.rotate(in, r, rotateAngle);
        return r;
    }
}
