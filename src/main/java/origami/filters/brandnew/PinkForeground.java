package origami.filters.brandnew;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import origami.Filter;
import origami.filters.FilterWithPalette;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.*;

public class PinkForeground extends FilterWithPalette implements Filter {
    BackgroundSubtractorMOG2 bgSubtractor = Video.createBackgroundSubtractorMOG2();

    
    public int getHistory() {
        return bgSubtractor.getHistory();
    }

    
    public void setHistory(int history) {
        bgSubtractor.setHistory(history);
    }

    
    public int getNMixtures() {
        return bgSubtractor.getNMixtures();
    }

    
    public void setNMixtures(int nmixtures) {
        bgSubtractor.setNMixtures(nmixtures);
    }

    
    public double getBackgroundRatio() {
        return bgSubtractor.getBackgroundRatio();
    }

    
    public void setBackgroundRatio(double ratio) {
        bgSubtractor.setBackgroundRatio(ratio);
    }

    
    public double getVarThreshold() {
        return bgSubtractor.getVarThreshold();
    }

    
    public void setVarThreshold(double varThreshold) {
        bgSubtractor.setVarThreshold(varThreshold);
    }

    
    public double getVarThresholdGen() {
        return bgSubtractor.getVarThresholdGen();
    }

    
    public void setVarThresholdGen(double varThresholdGen) {
        bgSubtractor.setVarThresholdGen(varThresholdGen);
    }

    
    public double getVarInit() {
        return bgSubtractor.getVarInit();
    }

    
    public void setVarInit(double varInit) {
        bgSubtractor.setVarInit(varInit);
    }

    
    public double getVarMin() {
        return bgSubtractor.getVarMin();
    }

    
    public void setVarMin(double varMin) {
        bgSubtractor.setVarMin(varMin);
    }

    
    public double getVarMax() {
        return bgSubtractor.getVarMax();
    }

    
    public void setVarMax(double varMax) {
        bgSubtractor.setVarMax(varMax);
    }

    
    public double getComplexityReductionThreshold() {
        return bgSubtractor.getComplexityReductionThreshold();
    }

    
    public void setComplexityReductionThreshold(double ct) {
        bgSubtractor.setComplexityReductionThreshold(ct);
    }

    
    public boolean getDetectShadows() {
        return bgSubtractor.getDetectShadows();
    }

    
    public void setDetectShadows(boolean detectShadows) {
        bgSubtractor.setDetectShadows(detectShadows);
    }

    
    public int getShadowValue() {
        return bgSubtractor.getShadowValue();
    }

    
    public void setShadowValue(int value) {
        bgSubtractor.setShadowValue(value);
    }

    
    public double getShadowThreshold() {
        return bgSubtractor.getShadowThreshold();
    }

    
    public void setShadowThreshold(double threshold) {
        bgSubtractor.setShadowThreshold(threshold);
    }

    public int maxContour = -1;

    public int getMaxContour() {
        return maxContour;
    }

    public void setMaxContour(int maxContour) {
        this.maxContour = maxContour;
    }

    public Mat apply(Mat inputImage) {


        // Convert the image to grayscale
        Mat grayImage = new Mat();
        cvtColor(inputImage, grayImage, COLOR_BGR2GRAY);

        // Perform background subtraction using a predefined algorithm
        Mat fgMask = new Mat();

        bgSubtractor.apply(grayImage, fgMask);

        // Convert the foreground mask to binary
        Mat binaryMask = new Mat();
        threshold(fgMask, binaryMask, 128, 255, THRESH_BINARY);

        Mat outputImage = new Mat(inputImage.size(), inputImage.type(), this.palette.ratioColor(0));
        outputImage.setTo(new Scalar(0,0,0), binaryMask);

        // Find contours in the binary mask
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(binaryMask, contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);

        int loopMax = maxContour != -1 ? Math.min(maxContour,contours.size()) : contours.size();
        // Draw filled pink shapes for each contour
        for (int i = 0; i < loopMax; i++) {
            drawContours(outputImage, contours, i, this.palette.ratioColor(0.2), -1);
        }

        return outputImage;
    }
}
