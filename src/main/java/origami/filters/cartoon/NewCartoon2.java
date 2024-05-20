package origami.filters.cartoon;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.core.Core.bitwise_not;
import static org.opencv.core.Core.subtract;
import static org.opencv.imgproc.Imgproc.*;

public class NewCartoon2 implements Filter {
    public int kmeans = 8;

    public int getKmeans() {
        return kmeans;
    }

    public void setKmeans(int kmeans) {
        this.kmeans = kmeans;
    }

    public int blur = 7;

    public int getBlur() {
        return blur;
    }

    public void setBlur(int blur) {
        this.blur = blur;
    }

    public int getAperture() {
        return aperture;
    }

    public void setAperture(int aperture) {
        this.aperture = aperture;
    }

    public int aperture = 3;

    public int lineSize = 17;

    public int getLineSize() {
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }

    @Override
    public Mat apply(Mat src) {

        // Convert the image to grayscale
        Mat gray = new Mat();
        cvtColor(src, gray, COLOR_BGR2GRAY);

        // Apply a median blur to reduce noise
        Mat blurred = new Mat();
        medianBlur(gray, blurred, blur);

        // Detect edges using Canny edge detector
        Mat edges = new Mat();
        Canny(blurred, edges, 50, 150, aperture);

        // Dilate the edges to make them thicker
        Mat dilatedEdges = new Mat();
        Mat kernel = getStructuringElement(MORPH_RECT, new Size(lineSize, lineSize));
        dilate(edges, dilatedEdges, kernel);

        // Invert the edges to get a black and white image
        Mat invertedEdges = new Mat();
        Core.bitwise_not(dilatedEdges, invertedEdges);

        // Apply a binary threshold to get a binary image
        Mat bwImage = new Mat();
        threshold(invertedEdges, bwImage, 100, 255, THRESH_BINARY);

        // Convert original image to Lab color space for quantization
        Mat labImage = new Mat();
        cvtColor(src, labImage, COLOR_BGR2Lab);

        // Reshape the image for k-means
        Mat reshapedImage = labImage.reshape(1, labImage.cols() * labImage.rows());
        reshapedImage.convertTo(reshapedImage, CvType.CV_32F);

        // Apply k-means clustering
        Mat labels = new Mat();
        Mat centers = new Mat();
        Core.kmeans(reshapedImage, kmeans, labels, new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 100, 1.0), 3, Core.KMEANS_PP_CENTERS, centers);

        // Convert back to 8-bit image
        centers.convertTo(centers, CvType.CV_8UC1);
        centers = centers.reshape(3);

        Mat quantizedImage = new Mat(labImage.size(), labImage.type());

        int rows = labImage.rows();
        int cols = labImage.cols();
        int channels = quantizedImage.channels();

        int[] labelArray = new int[rows * cols];
        labels.get(0, 0, labelArray);

        byte[] data = new byte[rows * cols * channels];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int clusterIdx = labelArray[i * cols + j];
                byte[] centerColor = new byte[3];
                centers.get(clusterIdx, 0, centerColor);

                // Convert to pastel color
                centerColor[0] = (byte) ((centerColor[0] & 0xFF + 50) / 2);  // Pastel effect on L channel
                centerColor[1] = (byte) ((centerColor[1] & 0xFF + 50) / 2);  // Reduce the effect on a channel for pastel
                centerColor[2] = (byte) ((centerColor[2] & 0xFF + 50) / 2);  // Reduce the effect on b channel for pastel

                int index = (i * cols + j) * channels;
                data[index] = centerColor[0];
                data[index + 1] = centerColor[1];
                data[index + 2] = centerColor[2];
            }
        }


        quantizedImage.put(0, 0, data);
        quantizedImage = quantizedImage.reshape(3, labImage.rows());
        Imgproc.cvtColor(quantizedImage, quantizedImage, Imgproc.COLOR_Lab2BGR);

        // Convert binary edges to 3-channel image
        Mat colorEdges = new Mat();
        Imgproc.cvtColor(bwImage, colorEdges, Imgproc.COLOR_GRAY2BGR);

        // Combine the edges with the quantized pastel image
        Mat cartoonImage = new Mat();
        Core.subtract(quantizedImage, colorEdges, cartoonImage);

        return cartoonImage;
    }
}
