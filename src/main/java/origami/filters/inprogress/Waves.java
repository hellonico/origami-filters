package origami.filters.inprogress;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.colors.HTML;

public class Waves implements Filter {
    @Override
    public Mat apply(Mat inputImage) {

        // Calculate the width and height of the subsection
        int subWidth = (int) (inputImage.width() * 0.8);
        int subHeight = (int) (inputImage.height() * 0.8);

        // Define the rectangle for the subsection
        Rect subRect = new Rect(0, 0, subWidth, subHeight);

        // Extract the subsection from the input image
        Mat subImage = new Mat(inputImage, subRect);

        // Create a reflection effect on the remaining 20% of the image
        Mat reflectionImage = new Mat(subImage.size(), subImage.type());
        reflectionImage.setTo(HTML.toScalar("black"));
        Core.flip(subImage, reflectionImage, 0); // Flip the image vertically (reflection effect)

        // Create a mask for the reflection
        Mat mask = new Mat(reflectionImage.size(), CvType.CV_8UC1, new Scalar(255));
        int gradientHeight = (int) (subHeight * 0.2);
        for (int y = 0; y < gradientHeight; y++) {
            double alpha = 1.0 - (double) y / gradientHeight;
            Imgproc.line(mask, new org.opencv.core.Point(0, y), new org.opencv.core.Point(subWidth - 1, y), new Scalar(0), 1);
            Core.addWeighted(reflectionImage.row(y), alpha, subImage.row(y), 1.0 - alpha, 0.0, reflectionImage.row(y));
        }

        // Create the final image by concatenating the subsection and the reflection
        Mat finalImage = new Mat(inputImage.size(), inputImage.type());
        Mat topHalf = new Mat(finalImage, new Rect(0, 0, subWidth, subHeight));
        subImage.copyTo(topHalf);

        Mat bottomHalf = new Mat(finalImage, new Rect(0, subHeight, subWidth, inputImage.height()-subHeight));
        reflectionImage.copyTo(bottomHalf);

        return finalImage;

    }
}
