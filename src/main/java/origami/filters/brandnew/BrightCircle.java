package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;
import origami.Filter;
import origami.colors.HTML;
import origami.utils.Utils;

import java.util.List;

public class BrightCircle implements Filter {

    public static class WithHaar extends BrightCircle {

        double scaleFactor = 1.1;
        int minNeighbors = 2;
        Size minSize = new Size(300, 300);
        Size maxSize = new Size(500, 500);


        public void setMinSize(int size) {
            this.minSize = new Size(size, size);
        }

        public int getMinSize() {
            return (int) this.minSize.height;
        }


        public void setMaxSize(int size) {
            this.maxSize = new Size(size, size);
        }

        public int getMaxSize() {
            return (int) this.maxSize.height;
        }

        String type;
        CascadeClassifier classifier;

        public WithHaar() {
            setType("haar.frontalface");
        }

        public void setType(String type) {
            this.type = type;
            classifier = Utils.loadCascadeClassifier(type);
        }

        public String getType() {
            return this.type;
        }

        public List<Rect> detectROI(Mat input) {
            MatOfRect faces = new MatOfRect();
            classifier.detectMultiScale(input, faces, scaleFactor, minNeighbors, -1, minSize, maxSize);
            return faces.toList();
        }

        public Mat apply(Mat image) {
            List<Rect> faces = detectROI(image);

            Mat imageCopy = image.clone();

            // Apply increased brightness to detected face regions
            for (Rect rect : faces) {
                Point _center = new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
                int radius = Math.max(rect.width, rect.height) / 2;
                increaseBrightness(imageCopy, _center, radius, brightnessOffset);
            }

            return imageCopy;
        }
    }

    int radius = 100;
    Scalar color = HTML.toScalar("white");

    int brightnessOffset = 50;

    int thickness = -1; // Filled circle

    Point center = new Point(100, 100);

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getColor() {
        return HTML.toHTML(color);
    }

    public void setColor(String color) {
        this.color = HTML.toScalar(color);
    }

    public int getBrightnessOffset() {
        return brightnessOffset;
    }

    public void setBrightnessOffset(int brightnessOffset) {
        this.brightnessOffset = brightnessOffset;
    }

    public String getCenter() {
        return Utils.Point_String(center);
    }

    public void setCenter(String center) {
        this.center = Utils.String_Point(center);
    }

    @Override
    public Mat apply(Mat image) {

        // Create a copy of the image to draw on
        Mat imageCopy = image.clone();

        // Draw the circle on the image
        // circle(imageCopy, center, radius, color, thickness);

        // Increase the brightness of the circle
        increaseBrightness(imageCopy, center, radius, brightnessOffset);

        return imageCopy;
    }

    private static void increaseBrightness(Mat image, Point center, int radius, int brightnessOffset) {
        int centerX = (int) center.x;
        int centerY = (int) center.y;

        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

                if (distance <= radius) {
                    double[] pixel = image.get(y, x);
                    for (int i = 0; i < pixel.length; i++) {
                        pixel[i] = Math.min(pixel[i] + brightnessOffset, 255);
                    }
                    image.put(y, x, pixel);
                }
            }
        }
    }

}
