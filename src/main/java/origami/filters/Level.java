package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import origami.Filter;

public class Level implements Filter {

    int n = 100;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Mat apply(Mat im) {
        Size sz = im.size();
        for (int i = 0; i < sz.height; i++) {
            for (int j = 0; j < sz.width; j++) {
                double[] pixel = im.get(i, j);
                pixel[0] = ((int) pixel[0] / n) * n + n / 2;
                pixel[1] = ((int) pixel[1] / n) * n + n / 2;
                pixel[2] = ((int) pixel[2] / n) * n + n / 2;
                im.put(i, j, pixel);
            }
        }
        return im;
    }
}
