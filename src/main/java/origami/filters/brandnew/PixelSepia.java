package origami.filters.brandnew;

import org.opencv.core.Mat;
import origami.Filter;

public class PixelSepia implements Filter {
    int depth = 20;
    int intensity = 30;

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public Mat apply(Mat img) {
        Mat mat = new Mat(img.size(), img.type());

        for (int y = 0; y < img.width(); y++) {
            for (int x = 0; x < img.height(); x++) {

                double[] pixel = img.get(x, y);
                double blue = pixel[0];
                double green = pixel[1];
                double red = pixel[2];
                double avg = (red + green + blue) / 3;
                int depth = this.depth;
                int intensity = this.intensity;

                red = avg + (depth * 2);
                green = avg + depth;
                blue = avg - intensity;

                if (red > 255) red = 255;
                if (green > 255) green = 255;
                if (blue > 255) blue = 255;
                if (blue < 0) blue = 0;
                mat.put(x, y, blue, green, red);
            }
        }
        return mat;
    }
}
