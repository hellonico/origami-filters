package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.Origami;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class Resize implements Filter {
    double width = -1;
    double height = -1;
    double factor = -1;

    public Resize() {

    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public Mat apply(Mat frame) {
        if(factor==-1 && height == -1 && width == -1)
            return frame;
        Size s = factor != -1 ?
                new Size(frame.size().width * factor, frame.size().height * factor) :
                new Size(width, height);
        Mat target = new Mat();
        Imgproc.resize(frame, target, s);
        return target;
    }

    public static void main(String[] args) {
        Origami.init();
        Mat m = imread("book.jpg");
        Resize f = new Resize();
        f.setFactor(0.2);
        imwrite("book2.jpg", f.apply(m));

        Filter f2 = Origami.StringToFilter("[{:class origami.Filters$NoOP}]");
        imwrite("book3.jpg", f2.apply(m));

        Filter f3 = Origami.StringToFilter("[{:class origami.filters.Resize :factor 0.2}]");
        imwrite("book4.jpg", f3.apply(m));
    }
}
