package origami.filters;

import org.opencv.core.Mat;
import org.opencv.photo.Photo;
import origami.Filter;

/**
 * http://www.cse.cuhk.edu.hk/leojia/projects/color2gray/index.html
 */
public class Decolor implements Filter {
    boolean gray = true;

    public boolean isGray() {
        return gray;
    }

    public void setGray(boolean gray) {
        this.gray = gray;
    }

    @Override
    public Mat apply(Mat mat) {
        Mat gray_ = new Mat();
        Mat color = new Mat();
        Photo.decolor(mat, gray_, color);
        if(gray) {
            return gray_;
        } else {
            return color;
        }
    }
}
