package origami.filters.cartoon;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import static org.opencv.core.Core.bitwise_and;
import static org.opencv.imgproc.Imgproc.*;

public class Cartoon implements Filter {

    public int d = 13;
    public int sigmaColor = d;
    public int sigmaSpace = 7;
    public int ksize = 7;

    public double maxValue = 255;
    public int blockSize = 9;
    public int C = 2;

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

    public int getKsize() {
        return ksize;
    }

    public void setKsize(int ksize) {
        this.ksize = ksize;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        C = c;
    }

    public Cartoon(int d, int sigmaColor, int sigmaSpace, int ksize, double maxValue, int blockSize, int c) {
        this.d = d;
        this.sigmaColor = sigmaColor;
        this.sigmaSpace = sigmaSpace;
        this.ksize = ksize;
        this.maxValue = maxValue;
        this.blockSize = blockSize;
        C = c;
    }

    public Cartoon() {

    }


    public Mat apply(Mat inputFrame) {
        Mat gray = new Mat();
        Mat co = new Mat();
        Mat m = new Mat();
        Mat mOutputFrame = new Mat();

        cvtColor(inputFrame, gray, Imgproc.COLOR_BGR2GRAY);
        bilateralFilter(gray, co, d, sigmaColor, sigmaSpace);
        Mat blurred = new Mat();
//         Imgproc.medianBlur(co, blurred, ksize);
        blur(co, blurred, new Size(ksize, ksize));
        adaptiveThreshold(blurred, blurred, maxValue, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,
                blockSize, C);

        cvtColor(blurred, m, Imgproc.COLOR_GRAY2BGR);
        bitwise_and(inputFrame, m, mOutputFrame);
        return mOutputFrame;
    }
}
