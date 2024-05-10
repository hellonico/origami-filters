package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import origami.Filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.opencv.core.Core.add;
import static org.opencv.core.CvType.CV_8UC3;
import static origami.filters.brandnew.GradienteVermelho.Gradients.expandHorizontally;

public class GradienteVermelho implements Filter {

    public boolean flip = false;

    public String name = "blue";

    public boolean vertical = false;

    public Mat apply(Mat image) {
        if (vertical) {
            Mat gradient = Gradients.GetGradient(name, image.height(), flip);
            gradient = gradient.reshape(3, image.height());
            add(image, Gradients.expandVertically(image, gradient), image);
            return image;
        } else {
            Mat gradient = Gradients.GetGradient(name, image.width(), flip);
            add(image, expandHorizontally(image, gradient), image);
            return image;
        }
    }

    public static class Horizontal extends GradienteVermelho {

        public Horizontal() {
            super();
            flip = true;
            name = "blue";
            vertical = false;
        }

    }

    public static class Vertical extends GradienteVermelho {
        public Vertical() {
            super();
            flip = false;
            name = "blue2";
            vertical = true;
        }
    }

    static class Gradients {
        static Mat expandVertically(Mat source, Mat gradient) {
            Mat verticalGradient = new Mat(source.size(), CvType.CV_8UC3);
            for (int x = 0; x < source.width(); x++) {
                gradient.copyTo(verticalGradient.col(x));
            }
            return verticalGradient;
        }

        static Mat expandHorizontally(Mat source, Mat gradient) {
            Mat redGradient = new Mat(source.size(), CV_8UC3);
            for (int y = 0; y < source.height(); y++) {
                gradient.copyTo(redGradient.row(y));
            }
            return redGradient;
        }

        static Mat GetGradient(String name, int size, boolean flip) {
            Method[] methods = Gradients.class.getMethods();
            Method m = Arrays.stream(methods).filter(e -> {
                return e.getName().equalsIgnoreCase(name);
            }).findFirst().get();
            try {
                Mat g = (Mat) m.invoke(null, size);
                if (flip) {
                    Mat gt = new Mat(g.size(), g.type());
                    Core.flip(g, gt, 1);
                    return gt;
                } else {
                    return g;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static Mat blue(int size) {
            Mat gradient = new Mat(1, size, CV_8UC3);
            for (int x = 0; x < size; x++) {
                double intensity = (double) x / size * 255;
                gradient.put(0, x, intensity, 0, 0);
            }
            return gradient;
        }

        public static Mat yellow(int size) {
            Mat gradient = new Mat(1, size, CV_8UC3);
            for (int x = 0; x < size; x++) {
                double intensity = (double) x / size * 255;
                gradient.put(0, x, 0, 255 - intensity / 1.5, 255 - intensity / 2);
            }
            return gradient;
        }

        public static Mat blue2(int size) {
            Mat gradient = new Mat(1, size, CV_8UC3);
            for (int x = 0; x < size; x++) {
                double intensity1 = Math.sin(Math.PI * x / size) * 64;
                gradient.put(0, x, intensity1, 0, intensity1);
            }
            return gradient;
        }

        public static Mat cosred(int size) {
            Mat gradient = new Mat(1, size, CV_8UC3);
            for (int x = 0; x < size; x++) {
                double intensity1 = Math.sin(Math.PI * x / size) * 64;
                double intensity2 = Math.cos(Math.PI * x / size) * 255;
                gradient.put(0, x, intensity2, 0, intensity1);
            }
            return gradient;
        }
    }

}
