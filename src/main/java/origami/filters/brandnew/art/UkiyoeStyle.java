package origami.filters.brandnew.art;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.Origami;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class UkiyoeStyle implements Filter {
    public double saturation = 10;
    public double brightness = 10;

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    public double getSaturation() {
        return saturation;
    }

    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    public int maxCount = 50;
    public int attempts = 3;
    public boolean adjustColorPalette = true;
    public int clusters = 64;

    public int getClusters() {
        return clusters;
    }

    public void setClusters(int clusters) {
        this.clusters = clusters;
    }

    public boolean getAdjustColorPalette() {
        return adjustColorPalette;
    }

    public void setAdjustColorPalette(boolean adjustColorPalette) {
        this.adjustColorPalette = adjustColorPalette;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    private Mat colorQuantization(Mat source) {
        Mat destination = new Mat();
        Mat samples = source.reshape(1, source.cols() * source.rows());
        samples.convertTo(samples, CvType.CV_32F);

        Mat labels = new Mat();
        Mat centers = new Mat();
        TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, maxCount, 0.1);
        kmeans(samples, clusters, labels, criteria, attempts, KMEANS_PP_CENTERS, centers);

        centers.convertTo(centers, CvType.CV_8U);
        centers.reshape(3);

        for (int i = 0; i < samples.rows(); i++) {
            int clusterIdx = (int) labels.get(i, 0)[0];
            samples.put(i, 0, centers.get(clusterIdx, 0));
        }

        destination.create(source.size(), source.type());
        samples.reshape(3, source.rows()).convertTo(destination, CvType.CV_8U);

        return destination;
    }


    @Override
    public Mat apply(Mat image) {


        // Step 1: Histogram Equalization
        Mat equalized = new Mat();
        cvtColor(image, equalized, COLOR_BGR2GRAY);
        equalizeHist(equalized, equalized);
        cvtColor(equalized, equalized, COLOR_GRAY2BGR);

        // Step 2: Apply Bilateral Filter
        Mat bilateral = new Mat();
        bilateralFilter(equalized, bilateral, 9, 75, 75);

        // Step 3: Color quantization
        Mat quantized = colorQuantization(bilateral); // Reduce to 64 colors

        // Step 4: Adjust color palette
        if(adjustColorPalette) {
            adjustColorPalette(quantized);
        }
        // Step 5: Enhance saturation and brightness
        enhanceSaturationAndBrightness(quantized);
//
//        // Step 3: Overlay edges
//        Mat edgesBGR = new Mat();
//        cvtColor(quantized, edgesBGR, COLOR_GRAY2BGR);
//        bitwise_not(edgesBGR, edgesBGR); // Invert edges
//        addWeighted(quantized, 1, edgesBGR, 0.5, 0, quantized);

        // Step 6: Edge detection
        Mat edges = new Mat();
        cvtColor(image, edges, COLOR_BGR2GRAY);
        GaussianBlur(edges, edges, new Size(3, 3), 0);
        Canny(edges, edges, 50, 150);

        // Step 5: Custom edge enhancement
        Mat enhancedEdges = new Mat();
        dilate(edges, enhancedEdges, getStructuringElement(MORPH_RECT, new Size(3, 3)));
        bitwise_not(enhancedEdges, enhancedEdges);
        Mat enhancedEdgesBGR = new Mat();
        cvtColor(enhancedEdges, enhancedEdgesBGR, COLOR_GRAY2BGR);
        addWeighted(quantized, 1, enhancedEdgesBGR, 0.7, 0, quantized);


        // Step 6: Enhance saturation and brightness
        enhanceSaturationAndBrightness(quantized);

        // Step 6: Texture overlay (optional)
        resize(texture, texture, quantized.size());
        addWeighted(quantized, 0.5, texture, 0.5, 0, quantized);

        // Save the result
        return quantized;
    }

    private static void adjustColorPalette(Mat image) {
        // Convert to LAB color space for easier color adjustment
        Mat labImage = new Mat();
        Imgproc.cvtColor(image, labImage, Imgproc.COLOR_BGR2Lab);

        // Split into L, A and B channels
        List<Mat> labChannels = new ArrayList<>();
        Core.split(labImage, labChannels);
        Mat l = labChannels.get(0);
        Mat a = labChannels.get(1);
        Mat b = labChannels.get(2);

        // Adjust the color channels to balance the color better
        Core.add(l, new Scalar(20), l); // Lighten the image more
        Core.add(a, new Scalar(10), a); // Add less red
        Core.add(b, new Scalar(10), b); // Add less yellow

        // Merge channels back and convert to BGR
        Core.merge(labChannels, labImage);
        Imgproc.cvtColor(labImage, image, Imgproc.COLOR_Lab2BGR);
    }

    private void enhanceSaturationAndBrightness(Mat image) {
        // Convert to HSV color space
        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);

        // Split into H, S and V channels
        List<Mat> hsvChannels = new ArrayList<>();
        Core.split(hsvImage, hsvChannels);
        Mat h = hsvChannels.get(0);
        Mat s = hsvChannels.get(1);
        Mat v = hsvChannels.get(2);

        // Enhance the saturation and brightness more precisely
        Core.add(s, new Scalar(saturation), s);
        Core.add(v, new Scalar(brightness), v);

        // Merge channels back and convert to BGR
        Core.merge(hsvChannels, hsvImage);
        Imgproc.cvtColor(hsvImage, image, Imgproc.COLOR_HSV2BGR);
    }
    static Mat texture = Origami.classPathFiletoMat("/white-paper-texture.jpg");
}
