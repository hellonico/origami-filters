package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static origami.filters.Utils.Point_String;
import static origami.filters.Utils.String_Point;

public class RotateWithMatrix implements Filter {
    double rotationAngle = 10.0;
    double scale = 1.0;

    public void setPoint(String point) {
        Point p = String_Point(point);
        this.point = p;
    }

    public String getPoint() {
        return Point_String(this.point);
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    Point point = new Point(50, 50);

    @Override
    public Mat apply(Mat image) {

        // Create a rotation matrix
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(point, rotationAngle, 1.0);

        // Rotate the bubble and the text
        Mat rotatedImage = new Mat();
        Imgproc.warpAffine(image, rotatedImage, rotationMatrix, image.size());

        return rotatedImage;
    }
}
