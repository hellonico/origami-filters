package origami.filters;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

public class ColorMap implements Filter {
    int color = 0;
//
//    public int getColor() {
//        return color;
//    }
//
//    public void setColor(int color) {
//        this.color = color;
//    }

    public ColorMap() {

    }

    public static class Autumn extends ColorMap {
        public Autumn() {
            super(Imgproc.COLORMAP_AUTUMN);
        }
    }

    public static class Bone extends ColorMap {
        public Bone() {
            super(Imgproc.COLORMAP_BONE);
        }
    }

    public static class Jet extends ColorMap {
        public Jet() {
            super(Imgproc.COLORMAP_JET);
        }
    }

    public static class Winter extends ColorMap {
        public Winter() {
            super(Imgproc.COLORMAP_WINTER);
        }
    }

    public static class Rainbow extends ColorMap {
        public Rainbow() {
            super(Imgproc.COLORMAP_RAINBOW);
        }
    }

    public static class Ocean extends ColorMap {
        public Ocean() {
            super(Imgproc.COLORMAP_OCEAN);
        }
    }

    public static class Summer extends ColorMap {
        public Summer() {
            super(Imgproc.COLORMAP_SUMMER);
        }
    }

    public static class Spring extends ColorMap {
        public Spring() {
            super(Imgproc.COLORMAP_SPRING);
        }
    }

    public static class Cool extends ColorMap {
        public Cool() {
            super(Imgproc.COLORMAP_COOL);
        }
    }

    public static class HSV extends ColorMap {
        public HSV() {
            super(Imgproc.COLORMAP_HSV);
        }
    }

    public static class Pink extends ColorMap {
        public Pink() {
            super(Imgproc.COLORMAP_PINK);
        }
    }

    public static class Hot extends ColorMap {
        public Hot() {
            super(Imgproc.COLORMAP_HOT);
        }
    }

    public static class Parula extends ColorMap {
        public Parula() {
            super(Imgproc.COLORMAP_PARULA);
        }
    }

    public static class Magma extends ColorMap {
        public Magma() {
            super(Imgproc.COLORMAP_MAGMA);
        }
    }

    public static class Viridis extends ColorMap {
        public Viridis() {
            super(Imgproc.COLORMAP_VIRIDIS);
        }
    }

    public static class Cividis extends ColorMap {
        public Cividis() {
            super(Imgproc.COLORMAP_CIVIDIS);
        }
    }

    public static class Twilight extends ColorMap {
        public Twilight() {
            super(Imgproc.COLORMAP_TWILIGHT);
        }
    }

    public static class TwilightShifted extends ColorMap {
        public TwilightShifted() {
            super(Imgproc.COLORMAP_TWILIGHT_SHIFTED);
        }
    }

    public static class Turbo extends ColorMap {
        public Turbo() {
            super(Imgproc.COLORMAP_TURBO);
        }
    }

    protected ColorMap(int colorMap) {
        this.color = colorMap;
    }

    @Override
    public Mat apply(Mat mat) {
        Mat result = new Mat();
        Imgproc.applyColorMap(mat, result, color);
        return result;
    }
}
