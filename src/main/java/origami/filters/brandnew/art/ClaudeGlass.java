package origami.filters.brandnew.art;

import org.opencv.core.*;
import origami.Filter;

import static org.opencv.core.Core.multiply;
import static org.opencv.imgproc.Imgproc.*;

public class ClaudeGlass implements Filter {


    public Mat apply(Mat image) {

        // Step 1: Convert to Grayscale
        Mat gray = new Mat();
        cvtColor(image, gray, COLOR_BGR2GRAY);

        // Step 2: Apply Gaussian Blur for a soft-focus effect
        Mat blurred = new Mat();
        GaussianBlur(gray, blurred, new Size(15, 15), 0);

        // Step 3: Create a vignette effect
        Mat vignette = createVignetteMask(image.size());

        // Step 4: Apply the vignette to darken edges
        Mat claudeGlassEffect = new Mat();
        multiply(blurred, vignette, claudeGlassEffect, 1.0 / 255.0);

        return claudeGlassEffect;

    }

    // Function to create a vignette mask
    private static Mat createVignetteMask(Size size) {
        int rows = (int) size.height;
        int cols = (int) size.width;
        Mat mask = new Mat(rows, cols, CvType.CV_8UC1, new Scalar(255));

        Point center = new Point(cols / 2.0, rows / 2.0);
        double maxDist = Math.sqrt(center.x * center.x + center.y * center.y);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                double dist = Math.sqrt(Math.pow(x - center.x, 2) + Math.pow(y - center.y, 2));
                double intensity = 255 * (1 - (dist / maxDist));  // Darker at edges
                mask.put(y, x, Math.max(intensity, 50));  // Ensure some brightness
            }
        }
        return mask;
    }
}
