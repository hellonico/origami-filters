package origami.filters;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.Origami;

import java.io.IOException;
import java.util.Map;

import static org.opencv.core.Core.addWeighted;

public class CrackedPaper implements Filter {

    Map<String,String> papers = Map.of("paper1","https://media.istockphoto.com/id/1333785971/photo/blank-white-crumpled-and-creased-paper-poster-texture.jpg?b=1&s=170667a&w=0&k=20&c=TaXAV2IwwfZXLIAe8LrjVP11kPVtZ_RPSamc2sZ5_pc=","paper2","https://st2.depositphotos.com/1006354/10094/i/950/depositphotos_100945234-stock-photo-antique-cracked-paper-texture.jpg");

    String paper;

    double alpha = 0.5;

    double beta = 0.5;

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    private double gamma= 0;

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;

        // Read the crack texture image
        try {
            crackTexture = Origami.urlToMat(papers.get(paper));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Mat crackTexture = new Mat();

    @Override
    public Mat apply(Mat image) {

        Mat resizedCrackTexture = new Mat();
        Imgproc.resize(crackTexture, resizedCrackTexture, image.size());

        Mat outImage = new Mat();

        addWeighted(image, alpha, resizedCrackTexture, beta, gamma, outImage);

        return outImage;
    }
}
