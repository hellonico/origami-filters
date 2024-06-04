package origami.filters.brandnew;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class FilmNoir implements Filter {
    @Override
    public Mat apply(Mat src) {
        // Convert to grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        // Increase contrast using histogram equalization
        Mat highContrast = new Mat();
        Imgproc.equalizeHist(gray, highContrast);

        // Create a vignette mask
        Mat vignette = createVignetteMask(highContrast.size());

        // Apply the vignette mask to the image

//        Mat filmNoir = new Mat();
        Imgproc.cvtColor(highContrast, highContrast, Imgproc.COLOR_GRAY2BGR);
        Mat filmNoir = new Vignetting().apply(highContrast);
//        Core.multiply(highContrast, vignette, filmNoir, 1.0 / 255);

        return filmNoir;
    }

    private static Mat createVignetteMask(Size size) {
        Mat vignette = Mat.ones(size, CvType.CV_32F);
        Point center = new Point(size.width / 2, size.height / 2);
        double maxDist = Math.sqrt(center.x * center.x + center.y * center.y);

        for (int i = 0; i < size.height; i++) {
            for (int j = 0; j < size.width; j++) {
                double dx = (j - center.x) / center.x;
                double dy = (i - center.y) / center.y;
                double dist = Math.sqrt(dx * dx + dy * dy);
                vignette.put(i, j, (1.0 - dist) * 255);
            }
        }

        return vignette;
    }

}
