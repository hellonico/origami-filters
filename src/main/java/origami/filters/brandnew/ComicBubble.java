package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComicBubble implements Filter {
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String text = "Hello!";

    public double fontScale = 1.0;

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public int thickness = 2;
    public int padding = 10;
    public int cornerRadius = 10;

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public double rotationAngle = 30;

    public double getFontScale() {
        return fontScale;
    }

    public void setFontScale(double fontScale) {
        this.fontScale = fontScale;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    @Override
    public Mat apply(Mat image) {

        int fontFace = Imgproc.FONT_HERSHEY_SIMPLEX;

        Size textSize = Imgproc.getTextSize(text, fontFace, fontScale, thickness, null);

        // Calculate the position and size of the bubble
        Point textPosition = new Point(padding, padding + textSize.height);
        int bubbleWidth = (int) (textSize.width + 2 * padding + 20); // Increased width
        int bubbleHeight = (int) (textSize.height + 2 * padding + 20); // Increased height
        Point bubblePosition = new Point(textPosition.x - padding, textPosition.y - textSize.height - padding);

        // Calculate the control points for the comic-style bubble shape
        Point topLeft = new Point(bubblePosition.x, bubblePosition.y + cornerRadius);
        Point topRight = new Point(bubblePosition.x + bubbleWidth, bubblePosition.y + cornerRadius);
        Point bottomRight = new Point(bubblePosition.x + bubbleWidth, bubblePosition.y + bubbleHeight - cornerRadius);
        Point bottomLeft = new Point(bubblePosition.x, bubblePosition.y + bubbleHeight - cornerRadius);
        Point[] controlPoints = {topLeft, topRight, bottomRight, bottomLeft};

        // Create a rotation matrix
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(textPosition, rotationAngle, 1.0);

        // Rotate the bubble and the text
        Mat rotatedImage = new Mat();
        Imgproc.warpAffine(image, rotatedImage, rotationMatrix, image.size());

        // Draw the comic-style text bubble
        Imgproc.ellipse(rotatedImage, new RotatedRect(topLeft, new Size(cornerRadius * 2, cornerRadius * 2), 0), new Scalar(255, 255, 255), Core.FILLED);
        Imgproc.ellipse(rotatedImage, new RotatedRect(topRight, new Size(cornerRadius * 2, cornerRadius * 2), 90), new Scalar(255, 255, 255), Core.FILLED);
        Imgproc.ellipse(rotatedImage, new RotatedRect(bottomRight, new Size(cornerRadius * 2, cornerRadius * 2), 180), new Scalar(255, 255, 255), Core.FILLED);
        Imgproc.ellipse(rotatedImage, new RotatedRect(bottomLeft, new Size(cornerRadius * 2, cornerRadius * 2), 270), new Scalar(255, 255, 255), Core.FILLED);
        List<Point> points = new ArrayList<>();
        points.addAll(Arrays.asList(controlPoints));
        MatOfPoint matOfPoint = new MatOfPoint();
        matOfPoint.fromList(points);
        List <MatOfPoint> contours = new ArrayList<>();
        contours.add(matOfPoint);
        Imgproc.fillPoly(rotatedImage, contours, new Scalar(255, 255, 255));
        Imgproc.putText(rotatedImage, text, textPosition, fontFace, fontScale, new Scalar(0, 0, 0), thickness);

        return rotatedImage;
    }
}
