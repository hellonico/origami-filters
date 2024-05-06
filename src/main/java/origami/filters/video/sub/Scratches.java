package origami.filters.video.sub;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.Random;

public class Scratches implements Filter {
    Random rand = new Random();
    public int numScratches = 100; // Adjust the number of scratches

    @Override
    public Mat apply(Mat inputImage) {

        for (int i = 0; i < numScratches; i++) {
            int startX = rand.nextInt(inputImage.cols());
            int startY = rand.nextInt(inputImage.rows());
            int endX = startX + rand.nextInt(50) + 5;
            int endY = startY + rand.nextInt(20) + 2;

            Imgproc.line(inputImage, new Point(startX, startY),
                    new Point(endX, endY), new Scalar(0, 0, 0), rand.nextInt(3) + 1);
        }
        return inputImage;
    }
}
