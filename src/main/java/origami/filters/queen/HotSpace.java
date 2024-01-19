package origami.filters.queen;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import origami.Filter;

import java.util.Arrays;
import java.util.List;

public class HotSpace implements Filter {

    int lower = 100;
    int upper = 200;

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

    @Override
    public Mat apply(Mat mat) {
        Queen.Freddy f1 = new Queen.Freddy();
        f1.setLower(this.getLower());
        f1.setUpper(this.getUpper());

        Queen.Brian f2 = new Queen.Brian();
        f2.setLower(this.getLower());
        f2.setUpper(this.getUpper());
        Queen.John f3 = new Queen.John();
        f3.setLower(this.getLower());
        f3.setUpper(this.getUpper());
        Queen.Roger f4 = new Queen.Roger();
        f4.setLower(this.getLower());
        f4.setUpper(this.getUpper());

        Mat freddy = f1.apply(mat);
        Mat brian = f2.apply(mat);
        Mat roger = f3.apply(mat);
        Mat john = f4.apply(mat);

        return ManyToOne(Arrays.asList(freddy, roger), Arrays.asList(john, brian));
    }

    private static Mat ManyToOne(List<Mat> topL, List<Mat> botomL) {
        Mat top = new Mat();
        Core.vconcat(topL, top);
        Mat bottom = new Mat();
        Core.vconcat(botomL, bottom);
        Mat result = new Mat();
        Core.hconcat(Arrays.asList(top, bottom), result);

        return result;
    }
}
