package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static origami.filters.Utils.*;

public class Annotate implements Filter {
    String text = "hello";
    Point point = new Point(50, 50);
    double fontSize = 3.0;
    Scalar color = new Scalar(255, 255, 255);
    int thickness = 3;
    int fontFace = Imgproc.FONT_HERSHEY_PLAIN;

    public void setPoint(String point) {
        Point p = String_Point(point);
        this.point = p;
    }

    public String getPoint() {
        return Point_String(this.point);
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public double getFontSize() {
        return this.fontSize;
    }

    public void setColor(String color) {
        this.color = String_Scalar(color);
    }

    public String getColor() {
        return Scalar_String(this.color);
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public int getThickness() {
        return this.thickness;
    }

    public void setText(String _text) {
        this.text = _text;
    }

    public String getText() {
        return this.text;
    }


    @Override
    public Mat apply(Mat mat) {
        Imgproc.putText(mat, text, point, fontFace, fontSize, color, thickness);
        return mat;
    }
}