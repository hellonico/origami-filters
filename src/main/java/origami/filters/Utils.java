package origami.filters;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static Point String_Point(String point) {
        String[] sp = point.split(",");
        return new Point(Double.parseDouble(sp[0]), Double.parseDouble(sp[1]));
    }

    public static String Point_String(Point p) {
        return p.x + "," + p.y;
    }


    public static Scalar String_Scalar(String scalar) {
        List<Double> sp =
                Arrays.stream(scalar.split(",")).map(Double::parseDouble).collect(Collectors.toList());
        if (sp.size() == 4) {
            return new Scalar(sp.get(0), sp.get(1), sp.get(2), sp.get(3));
        } else {
            return new Scalar(sp.get(0), sp.get(1), sp.get(2));
        }
    }

    public static String Scalar_String(Scalar s) {
        return s.val[0] + "," + s.val[1] + "," + s.val[2] + "," + s.val[3];
    }

    public static Size String_Size(String size) {
        List<Double> sp =
                Arrays.stream(size.split(",")).map(Double::parseDouble).collect(Collectors.toList());
        return new Size(sp.get(0), sp.get(1));
    }

    public static String Size_String(Size s) {
        return s.width + "," + s.height;
    }
}
