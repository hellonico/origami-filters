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
                "{:class origami.filters.brandnew.PixelSepia :depth 20 :intensity 50}",
                "{:class origami.filters.brandnew.FindContours :color \"#FCFCFC\"}",
                "{:class origami.filters.brandnew.Invert}",
                "{:class origami.filters.brandnew.Sobelo}"
        };
        for (String filter : filters) {
            Filter f = Origami.StringToFilter(filter);
            imwrite("build/"+f.getClass().getSimpleName()+".png", f.apply(Marcel));
        }

    }
}
