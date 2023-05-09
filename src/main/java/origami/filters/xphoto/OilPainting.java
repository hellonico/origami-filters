package origami.filters.xphoto;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.xphoto.Xphoto;
import origami.Filter;

public class OilPainting implements Filter {

    private int size = 10;
    private int ratio = 1;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code = Imgproc.COLOR_RGB2Lab;

    @Override
    public Mat apply(Mat mat) {
        Xphoto.oilPainting(mat, mat, size, ratio, code);
        return mat;
    }
}
