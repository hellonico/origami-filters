    package origami.filters.brandnew;

    import org.opencv.core.CvType;
    import org.opencv.core.Mat;
    import org.opencv.imgproc.Imgproc;
    import origami.Filter;

    import java.util.Random;


    public class OldTV implements Filter {
        public int val = 0;
        public int threshold = 80;
        Random rand = new Random();
        @Override
        public org.opencv.core.Mat apply(org.opencv.core.Mat img) {
            Mat gray = new Mat();
            Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
            gray.convertTo(gray, CvType.CV_8U);

            for (int i = 0; i < gray.rows(); i++) {
                for (int j = 0; j < gray.cols(); j++) {
                    if (rand.nextInt(100) <= threshold) {
                        double[] pixel = gray.get(i, j);
                        int noise = rand.nextInt(val + 1);
                        if (rand.nextBoolean()) {
                            pixel[0] = Math.min(pixel[0] + noise, 255);
                        } else {
                            pixel[0] = Math.max(pixel[0] - noise, 0);
                        }
                        gray.put(i, j, pixel);
                    }
                }
            }

            return gray;
        }
    }
