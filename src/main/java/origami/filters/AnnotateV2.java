package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import origami.Filter;

import static org.opencv.imgproc.Imgproc.FONT_HERSHEY_PLAIN;
import static org.opencv.imgproc.Imgproc.putText;
import static origami.colors.HTML.toHTML;
import static origami.colors.HTML.toScalar;
import static origami.utils.Utils.Point_String;
import static origami.utils.Utils.String_Point;

public class AnnotateV2 extends Annotate {

    @Override
    public Mat apply(Mat mat) {
        String[] lines = text.split("\\r?\\n");
        for (int i = 0; i < lines.length; i++) {
            Point p = point.clone();
            p.y = point.y + fontSize * i;
            putText(mat, lines[i], p, fontFace, fontSize, color, thickness);
        }

        return mat;
    }

}