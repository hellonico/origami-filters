package origami.filters;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import origami.Filter;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

public class BitwiseRed implements Filter {

    @Override
    public Mat apply(Mat frame) {

        Mat mask = new Mat(), mask1 = new Mat(), redMask = new Mat(),hsv = new Mat();
        cvtColor(frame, hsv, COLOR_BGR2HSV);
        inRange(hsv, new Scalar(0,70,50), new Scalar(10,255,255), mask);
        inRange(hsv, new Scalar(10,70,50), new Scalar(180,255,255), mask1);
        bitwise_or(mask, mask1, redMask);

        Mat result = new Mat();
        bitwise_and(frame,frame, result, redMask);

        Mat bw = new Mat();
        cvtColor(frame, bw, COLOR_BGR2GRAY);
        cvtColor(bw, bw, COLOR_GRAY2BGR);

        // Mat nRed = new Mat();
        // bitwise_not(redMask,nRed);

        copyTo(frame, bw, redMask);
        return bw;

    }
}
