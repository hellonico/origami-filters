package origami.filters.dip;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class ErodingDilating implements Filter {
    int erosionSize =5;

    int dilationSize = 5;

    public int getDilationSize() {
        return dilationSize;
    }

    public void setDilationSize(int dilationSize) {
        this.dilationSize = dilationSize;
    }

    public int getErosionSize() {
        return erosionSize;
    }

    public void setErosionSize(int erosionSize) {
        this.erosionSize = erosionSize;
    }

    @Override
    public Mat apply(Mat source) {
        Mat destination = new Mat();

        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(2 * erosionSize + 1, 2 * erosionSize + 1));
        Imgproc.erode(source, destination, element);

        Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(2 * dilationSize + 1, 2 * dilationSize + 1));
        Imgproc.dilate(source, destination, element1);
        return destination;
    }
}
