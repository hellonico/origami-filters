package origami.filters.brandnew.anime;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.core.Core.bitwise_and;
import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.imgproc.Imgproc.*;

public class Anime implements Filter {
    @Override
    public Mat apply(Mat src) {
        // Step 1: Apply bilateral filter to smooth the image
        Mat smoothed = new Mat();
        bilateralFilter(src, smoothed, d, sigmaColor, sigmaSpace);

        // Step 2: Convert to grayscale
        Mat gray = new Mat();
        cvtColor(smoothed, gray, COLOR_BGR2GRAY);

        // Step 3: Apply median blur to remove noise
        Mat blurred = new Mat();
        medianBlur(gray, blurred, ksize);

        // Step 4: Detect edges using adaptive thresholding
        Mat edges = new Mat();
        adaptiveThreshold(blurred, edges, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, blockSize, C);
//        adaptiveThreshold(blurred, edges, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY_INV, 9, 5);

        // Step 5: Dilate edges to make them thicker
        Mat kernel = getStructuringElement(MORPH_RECT, new Size(kernelSize, kernelSize));
        dilate(edges, edges, kernel);

        // Step 6: Convert edges to color image
        Mat edgesColor = new Mat();
        cvtColor(edges, edgesColor, COLOR_GRAY2BGR);

        // Step 6: Apply color quantization (using k-means clustering)
        Mat quantized = quantizeColors(smoothed, k);

        // Convert quantized to 8-bit
        quantized.convertTo(quantized, CV_8UC3);

        // Ensure edgesColor and quantized have the same size
        // Imgproc.resize(edgesColor, edgesColor, quantized.size());


        // Increase color vividness
        quantized = enhanceColors(quantized);


        // Step 7: Combine edges and quantized image
        Mat anime = new Mat();
        bitwise_and(quantized, edgesColor, anime);


        return anime;
    }

    public int k = 8;
    public int blockSize = 9;
    public int d = 9;
    public int sigmaColor = 75;
    public int sigmaSpace = 75;
    public double epsilon = 0.0001;
    public int maxCount = 10000;
    public int ksize = 7;

    public int getKernelSize() {
        return kernelSize;
    }

    public void setKernelSize(int kernelSize) {
        this.kernelSize = kernelSize;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        C = c;
    }

    public int kernelSize = 3;
    public int C = 5;

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getSigmaColor() {
        return sigmaColor;
    }

    public void setSigmaColor(int sigmaColor) {
        this.sigmaColor = sigmaColor;
    }

    public int getSigmaSpace() {
        return sigmaSpace;
    }

    public void setSigmaSpace(int sigmaSpace) {
        this.sigmaSpace = sigmaSpace;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getKsize() {
        return ksize;
    }

    public void setKsize(int ksize) {
        this.ksize = ksize;
    }

    //    private Mat quantizeColors(Mat src, int k) {
//        // Reshape the image to a 2D array of pixels
//        Mat data = src.reshape(1, src.rows() * src.cols());
//        data.convertTo(data, CV_32F);
//
//        // Apply k-means clustering
//        Mat labels = new Mat();
//        Mat centers = new Mat();
//        kmeans(data, k, labels,
//                new TermCriteria(EPS + MAX_ITER, maxCount, epsilon),
//                3, KMEANS_PP_CENTERS, centers);
//
//        // Convert back the centers to 8 bit values
//        centers.convertTo(centers, CV_8UC1);
//
//        // Replace pixel values with their corresponding cluster centers
//        for (int i = 0; i < data.rows(); i++) {
//            int clusterIdx = (int) labels.get(i, 0)[0];
//            data.put(i, 0, centers.get(clusterIdx, 0));
//            data.put(i, 1, centers.get(clusterIdx, 1));
//            data.put(i, 2, centers.get(clusterIdx, 2));
//        }
//
//        // Reshape back to the original image shape
//        Mat quantized = data.reshape(3, src.rows());
//        return quantized;
//    }
    private static Mat quantizeColors(Mat src, int k) {
        int rows = src.rows();
        int cols = src.cols();
        int channels = src.channels();

        byte[] data = new byte[rows * cols * channels];
        src.get(0, 0, data);

        Mat dataMat = new Mat(rows * cols, channels, CvType.CV_32F);
        int index = 0;
        for (int i = 0; i < rows * cols; i++) {
            for (int j = 0; j < channels; j++) {
                dataMat.put(i, j, (float) (data[index++] & 0xFF));
            }
        }

        Mat labels = new Mat();
        Mat centers = new Mat();
        Core.kmeans(dataMat, k, labels,
                new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 10000, 0.0001),
                3, Core.KMEANS_PP_CENTERS, centers);

        centers.convertTo(centers, CvType.CV_8UC1);

        byte[] quantizedData = new byte[rows * cols * channels];
        for (int i = 0; i < rows * cols; i++) {
            int clusterIdx = (int) labels.get(i, 0)[0];
            for (int j = 0; j < channels; j++) {
                quantizedData[i * channels + j] = (byte) centers.get(clusterIdx, j)[0];
            }
        }

        Mat quantized = new Mat(rows, cols, src.type());
        quantized.put(0, 0, quantizedData);
        return quantized;
    }


    private Mat enhanceColors(Mat src) {
        // Convert to HSV color space
        Mat hsv = new Mat();
        Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);

        // Split into channels
        java.util.List<Mat> hsvChannels = new java.util.ArrayList<>(3);
        Core.split(hsv, hsvChannels);

        // Increase saturation and value (brightness)
        Core.multiply(hsvChannels.get(1), new Scalar(1.5), hsvChannels.get(1)); // Increase saturation by 1.5x
        Core.multiply(hsvChannels.get(2), new Scalar(1.2), hsvChannels.get(2)); // Increase value (brightness) by 1.2x

        // Merge back into HSV image
        Core.merge(hsvChannels, hsv);

        // Convert back to BGR color space
        Mat enhanced = new Mat();
        Imgproc.cvtColor(hsv, enhanced, Imgproc.COLOR_HSV2BGR);

        // Split into BGR channels to increase blue component
        java.util.List<Mat> bgrChannels = new java.util.ArrayList<>(3);
        Core.split(enhanced, bgrChannels);

        // Increase blue channel by a factor
        Core.multiply(bgrChannels.get(0), new Scalar(blue), bgrChannels.get(0)); // Increase blue by 1.3x

        // Merge back into BGR image
        Core.merge(bgrChannels, enhanced);

        return enhanced;
    }
    public double blue = 1.3;

    public double getBlue() {
        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }
}
