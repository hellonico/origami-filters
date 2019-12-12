import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class Annotate implements Filter {
    String text = "hello";
    Point point = new Point(50,50);
    double fontSize = 3.0;
    Scalar color = new Scalar(255,255,255);
    int thickness = 3;

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public void setColor(Scalar color) {
        this.color = color;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public void setText(String _text) {
        this.text = _text;
    }
    @Override
    public Mat apply(Mat mat) {
        Imgproc.putText(mat,text, point,Imgproc.FONT_HERSHEY_PLAIN, fontSize, color,thickness);
        return mat;
    }
}