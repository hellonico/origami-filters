package origami.filters.inprogress;

import org.opencv.core.*;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;
import static origami.colors.HTML.toScalar;

public class OldMoviePoster implements Filter {
    public int colorQuantization = 3;
    public double saturation = 1.5;

    public int borderSize = 50;
    public Scalar borderColor = toScalar("black");

    public double fontScale = 3;
    public int thickness = 5;

    public double getFontScale() {
        return fontScale;
    }

    public void setFontScale(double fontScale) {
        this.fontScale = fontScale;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    String text = "You have been Mozart-ed";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }

    public Scalar getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = toScalar(borderColor);
    }

    public int getColorQuantization() {
        return colorQuantization;
    }

    public void setColorQuantization(int colorQuantization) {
        this.colorQuantization = colorQuantization;
    }

    public double getSaturation() {
        return saturation;
    }

    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    @Override
    public Mat apply(Mat src) {

        // Step 1: Reduce the number of colors
        Mat reducedColors = colorQuantization(src);

        // Step 2: Enhance vibrancy
        Mat vibrantImage = enhanceVibrancy(reducedColors);

        // Step 3: Add border
        Mat borderedImage = addBorder(vibrantImage);

        // Step 4: Add text
        if(!Objects.equals(text, "")) {
            return addText(borderedImage, text, true);
        } else {
            return borderedImage;
        }

    }

    public Mat colorQuantization(Mat src) {
        // Convert image to a 2D float array of pixels
        Mat samples = src.reshape(1, src.cols() * src.rows());
        samples.convertTo(samples, CvType.CV_32F);

        // Apply k-means clustering
        Mat labels = new Mat();
        Mat centers = new Mat();
        TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 100, 0.2);
        kmeans(samples, colorQuantization, labels, criteria, 3, KMEANS_PP_CENTERS, centers);

        // Convert back the labels to the original image
        centers.convertTo(centers, CvType.CV_8UC1);
        centers = centers.reshape(3);

        // Create the final image
        Mat new_image = new Mat(src.size(), src.type());
        for (int i = 0; i < src.rows(); i++) {
            for (int j = 0; j < src.cols(); j++) {
                int label = (int) labels.get(i * src.cols() + j, 0)[0];
                new_image.put(i, j, centers.get(label, 0));
            }
        }

        return new_image;
    }

    public Mat enhanceVibrancy(Mat src) {
        Mat hsv = new Mat();
        cvtColor(src, hsv, COLOR_BGR2HSV);

        List<Mat> channels = new ArrayList<>(3);
        split(hsv, channels);

        // Increase the saturation channel
        multiply(channels.get(1), new Scalar(saturation), channels.get(1)); // Increase by 50%

        merge(channels, hsv);
        Mat vibrantImage = new Mat();
        cvtColor(hsv, vibrantImage, COLOR_HSV2BGR);

        return vibrantImage;
    }

    public Mat addBorder(Mat src) {
        Mat borderedImage = new Mat();
        copyMakeBorder(src, borderedImage, borderSize, borderSize, borderSize, borderSize, BORDER_CONSTANT, borderColor);
        return borderedImage;
    }

    public Mat addText(Mat src, String text, boolean textAtBottom) {
        int fontFace = FONT_HERSHEY_SIMPLEX;

        // Get the text size
        Size textSize = getTextSize(text, fontFace, fontScale, thickness, new int[1]);
        int textWidth = (int) textSize.width;
        int textHeight = (int) textSize.height;

        // Calculate the position to center the text
        int x = (src.cols() - textWidth) / 2;
        int y = textAtBottom ? (src.rows() - textHeight) : (textHeight + 20);

        // Add the text
        putText(src, text, new Point(x, y), fontFace, fontScale, new Scalar(255, 255, 255), thickness);

        return src;
    }
}
