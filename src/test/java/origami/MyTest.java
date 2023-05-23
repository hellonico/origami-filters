package origami;

import org.junit.Test;
import org.opencv.core.Mat;
import origami.filters.brandnew.PixelSepia;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class MyTest {

    @Test
    public void helloSepia() {
        Origami.init();

        String matPath = MyTest.class.getClassLoader().getResource("marcel.jpg").getPath();
        Mat Marcel = imread(matPath);
        String[] filters = new String[]{
//                "{:class origami.filters.brandnew.PixelSepia :depth 20 :intensity 50}",
//                "{:class origami.filters.brandnew.FindContours :color \"#FCFCFC\"}",
//                "{:class origami.filters.brandnew.Invert}",
//                "{:class origami.filters.brandnew.Sobelo}",
//                  "{:class origami.filters.brandnew.GammaCorrection :gamma 5.5 }",
//                "{:class origami.filters.brandnew.BasicLinear}",
//                "{:class origami.filters.brandnew.WhiteBalanceProcessor}",
                // "{:class origami.filters.artcodes.HlsEditImageProcessor :repeat 2 :light 30 :hue -110 :saturation 150}"
//                "{:class origami.filters.queen.Queen$Brian}",
//                "{:class origami.filters.queen.Queen$Freddy}",
//                "{:class origami.filters.queen.Queen$John}",
//                "{:class origami.filters.queen.Queen$Roger}",
//                "{:class origami.filters.queen.HotSpace}",
//                "{:class origami.filters.queen.Queen$BlackWhite}"
//                "{:class origami.filters.queen.Queen :lower 10 :upper 30 :main \"#FFCC00\" :sub \"#aaCC00\"}"
//                "{:class origami.filters.cartoon.Manga2}",
                "{:class origami.filters.Histogram}",
        };
        for (String filter : filters) {
            Filter f = Origami.StringToFilter(filter);
            imwrite("build/"+f.getClass().getSimpleName()+".png", f.apply(Marcel));
        }

    }
}
