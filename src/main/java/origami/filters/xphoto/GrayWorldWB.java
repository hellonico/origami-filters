package origami.filters.xphoto;

import org.opencv.core.Mat;
import org.opencv.xphoto.Xphoto;
import origami.Filter;

public class GrayWorldWB implements Filter {


    org.opencv.xphoto.GrayworldWB tmD;

    public float getSaturationThreshold() {
        return tmD.getSaturationThreshold();
    }

    public void setSaturationThreshold(float saturationThreshold) {
        tmD.setSaturationThreshold(saturationThreshold);
    }

    public GrayWorldWB() {
        tmD = Xphoto.createGrayworldWB();
    }

    @Override
    public Mat apply(Mat mat) {
        Mat mat_ = new Mat();
        tmD.balanceWhite(mat,mat_);
        return mat_;
    }
}
