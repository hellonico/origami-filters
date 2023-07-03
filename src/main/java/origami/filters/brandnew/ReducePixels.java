package origami.filters.brandnew;

import org.opencv.core.Mat;
import origami.Filter;
import origami.colors.HTML;

public class ReducePixels implements Filter {

    public int pixelCount = 3;
    public String baseColor = "white";

    public int getPixelCount() {
        return pixelCount;
    }

    public void setPixelCount(int pixelCount) {
        this.pixelCount = pixelCount;
    }

    public String getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(String baseColor) {
        this.baseColor = baseColor;
    }

    @Override
    public Mat apply(Mat inputImage) {

        // Create a new Mat to store the filtered image
        Mat filteredImage = new Mat(inputImage.size(), inputImage.type());
        filteredImage.setTo(HTML.toScalar(baseColor));

        // Iterate over each pixel in the input image
        for (int y = 0; y < inputImage.rows(); y++) {
            for (int x = 0; x < inputImage.cols(); x++) {
                // Check if the pixel's coordinates are divisible by 3
                if (y % pixelCount == 0 && x % pixelCount == 0) {
                    // Get the pixel value from the input image
                    double[] pixel = inputImage.get(y, x);

                    // Set the pixel value in the filtered image
                    filteredImage.put(y, x, pixel);
                }
            }
        }
        return filteredImage;
    }
}
