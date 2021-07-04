package origami.filters.xphoto;

import org.opencv.core.Mat;
import org.opencv.xphoto.Xphoto;
import origami.Filter;

public class SimpleWB implements Filter {
    org.opencv.xphoto.SimpleWB swb;
    float inputMin = 0;
    float inputMax = 255;
    float outputMin = 0;
    float outputMax = 255;
    float p = 1;

    public float getInputMin() {
        return inputMin;
    }

    public void setInputMin(float inputMin) {
        this.inputMin = inputMin;
        swb.setInputMin(inputMin);
    }

    public float getInputMax() {
        return inputMax;
    }

    public void setInputMax(float inputMax) {
        this.inputMax = inputMax;
        swb.setInputMax(inputMax);
    }

    public float getOutputMin() {
        return outputMin;
    }

    public void setOutputMin(float outputMin) {
        this.outputMin = outputMin;
        swb.setOutputMin(outputMin);
    }

    public float getOutputMax() {
        return outputMax;
    }

    public void setOutputMax(float outputMax) {
        this.outputMax = outputMax;
        swb.setOutputMax(outputMax);
    }

    public float getP() {
        return p;
    }

    public void setP(float p) {
        this.p = p;
        swb.setP(p);
    }

    public SimpleWB() {
        swb = Xphoto.createSimpleWB();
    }

    @Override
    public Mat apply(Mat mat) {
        Mat rsc = new Mat();
        swb.balanceWhite(mat, rsc);
        return rsc;
    }
}

