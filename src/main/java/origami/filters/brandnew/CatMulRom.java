package origami.filters.brandnew;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class CatMulRom implements Filter {
    @Override
    public Mat apply(Mat input) {
        Mat image = input.clone();

        Mat floatImage = new Mat();
        image.convertTo(floatImage, CvType.CV_32F);
        // Apply the Catmull-Rom filter
        Mat filteredImage = new Mat();
        Imgproc.cvtColor(floatImage, floatImage, Imgproc.COLOR_BGR2RGB);
        Imgproc.resize(floatImage, filteredImage, new Size(), 2.0, 2.0, Imgproc.INTER_CUBIC);
        Imgproc.resize(filteredImage, filteredImage, new Size(), 0.5, 0.5, Imgproc.INTER_CUBIC);
        Imgproc.cvtColor(filteredImage, filteredImage, Imgproc.COLOR_RGB2BGR);

        // Convert the filtered image back to the original color format
        Mat outputImage = new Mat();
        filteredImage.convertTo(outputImage, CvType.CV_8UC3);

        return outputImage;

    }


}
