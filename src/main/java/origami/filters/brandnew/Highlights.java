package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;

public class Highlights implements Filter {
    public int highlights = 200;

    public int getHighlights() {
        return highlights;
    }

    public void setHighlights(int highlights) {
        this.highlights = highlights;
    }

    @Override
    public Mat apply(Mat mat) {
        Mat yuv = new Mat();
        Imgproc.cvtColor(mat, yuv, Imgproc.COLOR_BGR2YUV);

        List<Mat> channels = new ArrayList<Mat>();
        split(yuv, channels);

        Mat y = channels.get(0);
//        Mat u = channels.get(1);
//        Mat v = channels.get(2);

        Mat aa = new Mat(mat.size(), CvType.CV_8UC1);
        aa.setTo(new Scalar(this.highlights));
        bitwise_and(y, aa, y);

        merge(channels, yuv);

        Mat bgr = new Mat();
        Imgproc.cvtColor(yuv, bgr, Imgproc.COLOR_YUV2BGR);

        return bgr;
    }
}
