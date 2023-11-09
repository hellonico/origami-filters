package origami.filters.xphoto;

import org.opencv.core.Mat;
import org.opencv.photo.Photo;
import org.opencv.xphoto.Xphoto;
import origami.Filter;

public class DtcDenoising implements Filter {


    private double sigma = 21;
    private int psize = 6 ;

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public int getPsize() {
        return psize;
    }

    public void setPsize(int psize) {
        this.psize = psize;
    }

    @Override
    public Mat apply(Mat mat) {
        Xphoto.dctDenoising(mat, mat, sigma, psize);
        return mat;


    }
}
