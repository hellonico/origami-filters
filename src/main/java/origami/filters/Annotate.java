package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import origami.Filter;

import static org.opencv.imgproc.Imgproc.*;
import static origami.colors.HTML.*;
import static origami.utils.Utils.*;

public class Annotate implements Filter {
    String text = "hello";
    Point point = new Point(50, 50);
    double fontSize = 3.0;
    Scalar color = toScalar("white");
    int thickness = 3;
    int fontFace = FONT_HERSHEY_PLAIN;

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
        this.color = toScalar(color);
    }

    public String getColor() {
        return toHTML(this.color);
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
        putText(mat, text, point, fontFace, fontSize, color, thickness);
        return mat;
    }
}