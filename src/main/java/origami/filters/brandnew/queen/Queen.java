package origami.filters.brandnew.queen;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.colors.HTML;

public abstract class Queen implements Filter {
    Scalar main;
    Scalar sub;

    int lower = 10;
    int upper = 150;

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getUpper() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    public Queen() {

    }

    public String getMain() {
        return HTML.toHTML(main);
    }
    public String getSub() {
        return HTML.toHTML(sub);
    }
    public void setMain(String color) {
        main = HTML.toScalar(color);
    }
    public void setSub(String color) {
        sub = HTML.toScalar(color);
    }

    @Override
    public Mat apply(Mat mat) {
        Mat mask = new Mat();
        Mat hls = new Mat();

        Imgproc.cvtColor(mat, hls, Imgproc.COLOR_BGR2HSV);
        Core.inRange(hls, new Scalar(lower, 0, 0), new Scalar(upper, 255, 255), mask);

        Mat invertedMask = new Mat();
        Core.bitwise_not(mask,invertedMask);

        Mat result = new Mat(mat.size(),mat.type());
        result.setTo(sub, invertedMask);
        result.setTo(main, mask);

        return result;
    }

    public static class BlackWhite extends Queen {
        public BlackWhite() {
            super();
            setMain("#FFFFFF");
            setSub("#000000");
        }
    }

    public static class Freddy extends Queen {
        public Freddy() {
            super();
            setMain("#43007c");
            setSub("#ff0006");
        }
    }

    public static class John extends Queen {
        public John() {
            super();
            setMain("#fe2400");
            setSub("#049cc3");
        }
    }

    public static class Brian extends Queen {
        public Brian() {
            super();
            setMain("#c3c3c3");
            setSub("#fcff02");
        }
    }

    public static class Roger extends Queen {
        public Roger() {
            super();
            setMain("#fe2c99");
            setSub("#029020");
        }
    }
}
