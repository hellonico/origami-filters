package origami.filters.brandnew;

import org.opencv.core.*;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;

public class Vignetting implements Filter {

    private static Mat createVignetteMask(Size size, double strength) {
        Mat mask = Mat.ones(size, CvType.CV_8U);

        int rows = (int) size.height;
        int cols = (int) size.width;

        Point center = new Point(cols / 2.0, rows / 2.0);
        double maxRadius = Math.sqrt(center.x * center.x + center.y * center.y);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double distance = Math.sqrt(Math.pow(i - center.y, 2) + Math.pow(j - center.x, 2));
                double scale = Math.exp(-strength * Math.pow(distance / maxRadius, 2));
                mask.put(i, j, scale);
            }
        }

        // Convert to a 3-channel image by duplicating the mask for each color channel
        ArrayList<Mat> channels = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            channels.add(mask);
        }
        Mat mask3Channel = new Mat(size, CvType.CV_8UC3);
        merge(channels, mask3Channel);

        return mask3Channel;
    }

    @Override
    public Mat apply(Mat image) {

        // Create the vignetting mask
        Mat mask = createVignetteMask(image.size(), 2.5);

        // Create a darkened version of the image
        Mat darkImage = new Mat();
        image.convertTo(darkImage, -1, 0.5, 0); // Scale down the intensity

        // Blend the original image and the darkened image using the mask
        Mat result = new Mat();
        Core.addWeighted(image, 1.0, darkImage, 1.0, 0.0, result);

        // Apply the mask to the result
        Core.multiply(result, mask, result);

        return result;

    }
}
