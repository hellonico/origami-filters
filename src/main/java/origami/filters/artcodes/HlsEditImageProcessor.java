package origami.filters.artcodes;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.annotations.Parameter;

public class HlsEditImageProcessor implements Filter {

    @Parameter(description = "hue input range: [0,360] change to range: [0,180]")
    int hue = 0;
    @Parameter(description = "lightness input range: [-100,100] change to range: [-255,255]")
    int light = 0;

    @Parameter(description = "saturation input range: [-100,100] change to range: [-255,255]")
    int saturation = 30;

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    @Parameter(description = "Apply the filter many time")
    int repeat = 1;

    public int getHue() {
        return hue;
    }

    public void setHue(int hue) {
        this.hue = hue;
        lut = null;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
        lut = null;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
        lut = null;
    }

    Mat lut;

    private void prepare() {

        this.hue = (hue / 2) % 181;

        this.light = Math.min(Math.max((int) (light * 2.55), -255), 255);

        this.saturation = Math.min(Math.max((int) (saturation * 2.55), -255), 255);

        float lightnessMultiplyer = ((this.light + 255.0f) / 255.0f);
        float saturationMultiplyer = ((this.light + 255.0f) / 255.0f);

        this.lut = new Mat(1, 256, CvType.CV_8UC3);
        int lutSize = lut.cols() * lut.rows() * lut.channels();
        byte[] lutBuffer = new byte[lutSize];
        for (int i = 0, lutIndex = -1; i < 256; ++i) {
            lutBuffer[++lutIndex] = (byte) (((i) + this.hue) % 181);
            lutBuffer[++lutIndex] = (byte) Math.min(Math.max((int) ((i) * lightnessMultiplyer), 0), 255);
            lutBuffer[++lutIndex] = (byte) Math.min(Math.max((int) ((i) * saturationMultiplyer), 0), 255);
        }
        lut.put(0, 0, lutBuffer);

    }

    @Override
    public Mat apply(Mat mat) {
        if (lut == null) {
            prepare();
        }

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HLS);
        for (int i = 0 ; i < repeat ; i++) {
            Core.LUT(mat, lut, mat);
        }

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_HLS2BGR);
        return mat;
    }
}
