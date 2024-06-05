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

        // Convert highContrast to CV_32F
//        Mat highContrast32F = new Mat();
        highContrast.convertTo(highContrast, CvType.CV_64FC1);

        // Create a vignette mask
        Mat vignette = createVignetteMask(highContrast.size());

        // Apply the vignette mask to the image using addWeighted
        Mat filmNoir = new Mat();
        Core.addWeighted(highContrast, 0.5, vignette, 0.4, 0, filmNoir);

        // Convert filmNoir back to CV_8U
        Mat filmNoir8U = new Mat();
        filmNoir.convertTo(filmNoir8U, CvType.CV_8UC3);

        return filmNoir;
    }
    private static Mat createVignetteMask(Size size) {
        Mat kernelX = Imgproc.getGaussianKernel((int)size.width, size.width / 2.0);
        Mat kernelY = Imgproc.getGaussianKernel((int)size.height, size.height / 2.0);
        Mat kernelXY = new Mat();
        Core.gemm(kernelX, kernelY.t(), 1, new Mat(), 0, kernelXY);
        Core.normalize(kernelXY, kernelXY, 0, 1, Core.NORM_MINMAX);

        // Convert to a 3-channel image
        Mat vignette = new Mat(size, CvType.CV_32F);
        Core.multiply(kernelXY, new Scalar(255), vignette);

        // 謎
        return vignette.t();
    }
}
