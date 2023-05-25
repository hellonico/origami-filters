package origami.filters;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;
import origami.Filter;
import static org.opencv.imgproc.Imgproc.COLOR_GRAY2RGB;

public class BackgroundSubstractor implements Filter {
    boolean useMOG2 = false;
    BackgroundSubtractor backSub;
    public double learningRate = 1.0;
    public boolean showMask;

    public boolean isUseMOG2() {
        return useMOG2;
    }

    public void setUseMOG2(boolean useMOG2) {
        this.useMOG2 = useMOG2;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public boolean isShowMask() {
        return showMask;
    }

    public void setShowMask(boolean showMask) {
        this.showMask = showMask;
    }

    public BackgroundSubstractor() {
        if (useMOG2) {
            backSub = Video.createBackgroundSubtractorMOG2();
        } else {
            backSub = Video.createBackgroundSubtractorKNN();
        }
    }

    @Override
    public Mat apply(Mat in) {
        Mat mask = new Mat();
        backSub.apply(in, mask);
        Mat result = new Mat();
        if (showMask) {
            Imgproc.cvtColor(result, result, COLOR_GRAY2RGB);
            return result;
        } else {
            in.copyTo(result, mask);
            return result;
        }
    }
}
