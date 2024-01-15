package origami.filters.brandnew;

import org.opencv.core.Mat;
import origami.Filter;

public class Invert implements Filter {
    @Override
    public Mat apply(Mat img) {
        Mat mat = new Mat(img.size(), img.type());

        for (int y = 0; y < img.width(); y++) {
            for (int x = 0; x < img.height(); x++) {

                double[] pixel = img.get(x, y);
                double blue = pixel[0];
                double green = pixel[1];
                double red = pixel[2];

                red = 255 -red;
                green = 255- green;
                blue = 255-blue;

                if (blue < 0) blue = 0;
                if (red < 0) red = 0;
                if (green < 0) green = 0;

                mat.put(x, y, blue, green, red);
            }
        }
        return mat;
    }
}
