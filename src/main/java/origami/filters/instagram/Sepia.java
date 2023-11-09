package origami.filters.instagram;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import origami.Filter;

import static org.opencv.core.Core.transform;

/**
 * Using a kernel to get sepia picture
 */
public abstract class Sepia implements Filter {
    public static class Gray extends Sepia {
        public Gray() {
            super();
            kernel.put(0,0,
                    // rgb -> blue
                    0.272, 0.534, 0.131,
                    // rgb -> green
                    0.349, 0.686, 0.168,
                    // rgb -> red
                    0.393, 0.769, 0.189);
        }
    }

    public static class Red extends Sepia {
        public Red() {
            super();
            // mat is in BGR
            kernel.put(0, 0,
                    // green
                    0.272, 0.534, 0.131,
                    // blue
                    0.349, 0.686, 0.168,
                    // red
                    0.593, 0.769, 0.489);
        }
    }

    Mat kernel = new Mat(3, 3, CvType.CV_32F);

    private Sepia() {

    }

    public Mat apply(Mat source) {
        Mat destination = new Mat();
        transform(source, destination, kernel);
        return destination;
    }
}