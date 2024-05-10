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

        String matPath = MyTest.class.getClassLoader().getResource("marcel.png").getPath();
        Mat Marcel = imread(matPath);
        String[] filters = new String[]{
//                "{:class origami.filters.brandnew.PixelSepia :depth 20 :intensity 50}",
//                "{:class origami.filters.brandnew.FindContours :color \"#FCFCFC\"}",
//                "{:class origami.filters.brandnew.Invert}",
//                "{:class origami.filters.brandnew.Sobelo}",
//                  "{:class origami.filters.brandnew.GammaCorrection :gamma 5.5 }",
//                "{:class origami.filters.brandnew.BasicLinear}",
//                "{:class origami.filters.artcodes.WhiteBalanceProcessor}",
                // "{:class origami.filters.artcodes.HlsEditImageProcessor :repeat 2 :light 30 :hue -110 :saturation 150}"
//                "{:class origami.filters.queen.Queen$Brian}",
//                "{:class origami.filters.queen.Queen$Freddy}",
//                "{:class origami.filters.queen.Queen$John}",
//                "{:class origami.filters.queen.Queen$Roger}",
//                "{:class origami.filters.queen.HotSpace}",
//                "{:class origami.filters.queen.Queen$BlackWhite}"
//                "{:class origami.filters.queen.Queen :lower 10 :upper 30 :main \"#FFCC00\" :sub \"#aaCC00\"}"
//                "{:class origami.filters.brandnew.Manga2}",
                //"{:class origami.filters.brandnew.Histogram}",
                //"{:class origami.filters.brandnew.MostColors}",
//                "{:class origami.filters.inprogress.Kandinsky}",
//                "{:class origami.filters.inprogress.Kandinsky2}",
                // "{:class origami.filters.brandnew.FakeHDR, :gamma 1.2, :intensity 1.0, :colorAdaptation 130.0, :lightAdaptation 2.5}",
//                "{:class origami.filters.brandnew.Ghost}",
//                "{:class origami.filters.brandnew.Picasso}",
//                "{:class origami.filters.NoOP}",
//                "{:class origami.filters.brandnew.PinkForeground}",
//                "{:class origami.filters.inprogress.Waves}",
//                  "{:class origami.filters.video.VHS}",
//                  "{:class origami.filters.video.VHSEnhanced}",
//                "{:class origami.filters.video.OldFilm}",
//                "{:class origami.filters.video.Terminator}",
//                "{:class origami.filters.video.EarlyDaysOfColorTV}",
//                "{:class origami.filters.instagram.gpt.Gotham}",
//                "{:class origami.filters.instagram.gpt.Melbourne}",
//                "{:class origami.filters.instagram.gpt.Lark}",
//                "{:class origami.filters.instagram.gpt.Mayfair}",
//                "{:class origami.filters.instagram.gpt.XProII}",
//                "{:class origami.filters.instagram.gpt.ShadowEnhance}",
                "{:class origami.filters.brandnew.GradienteVermelho$Horizontal}",
                "{:class origami.filters.brandnew.GradienteVermelho$Vertical}",
                    "{:class origami.filters.brandnew.Matrix}",

        };
        for (String filter : filters) {
            Filter f = Origami.StringToFilter(filter);
            System.out.println("Loaded:"+f.getClass().getName());
            imwrite("build/"+f.getClass().getSimpleName()+".png", f.apply(Marcel.clone()));
        }

//        tonemapper.setGamma(1.2f);
//        tonemapper.setIntensity(1f);
//        tonemapper.setColorAdaptation(130.0f);
//        tonemapper.setLightAdaptation(0.5f);

    }
}
