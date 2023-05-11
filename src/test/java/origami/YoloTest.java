package origami;

import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import origami.filters.detect.yolo.MyYolo;
import origami.filters.detect.yolo.MyYolo.V3;

import java.io.IOException;

public class YoloTest {

    @Test
    public void testYolo() throws IOException {
        Origami.init();
        String url = "https://raw.githubusercontent.com/hellonico/origami-dnn/master/resources/marcel.jpg";
        Mat marcel = Origami.urlToMat(url);
        Filter f = new MyYolo.V2Tiny();
        Mat output = f.apply(marcel);
        Imgcodecs.imwrite("yolo.jpg",output);
    }
}
