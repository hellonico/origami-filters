package origami.filters.xphoto;

import org.opencv.core.Mat;
import org.opencv.xphoto.Xphoto;
import origami.Filter;

public class ChannelGains implements Filter {

    float Red = 1.0f;
    float Blue = 1.0f;
    float Green = 1.0f;

    public float getRed() {
        return Red;
    }

    public void setRed(float red) {
        Red = red;
    }

    public float getBlue() {
        return Blue;
    }

    public void setBlue(float blue) {
        Blue = blue;
    }

    public float getGreen() {
        return Green;
    }

    public void setGreen(float green) {
        Green = green;
    }

    @Override
    public Mat apply(Mat mat) {
        Xphoto.applyChannelGains(mat, mat, Blue, Green, Red);
        return mat;
    }
}
