package origami.filters.doge;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victo on 1/1/2018.
 */

public class LeviColorFilter implements Filter {
    public enum ColorPreset{
        RED,
        BLUE,
        YELLOW
    }
    private ColorPreset color = ColorPreset.RED;
    private double threshold = -1; // if -1 the color mode will use its own defaults
    private List<Mat> channels = new ArrayList<>();

    public String getColor() {
        return color.name();
    }

    public void setColor(String color) {
        this.color = ColorPreset.valueOf(color);
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    //
//    public LeviColorFilter(ColorPreset filterColor){
//        color = filterColor;
//    }
//
//    public LeviColorFilter(ColorPreset filterColor, double _filterThreshold){
//        color = filterColor;
//        threshold = _filterThreshold;
//    }

    public LeviColorFilter() {
    }

    @Override
    public Mat apply(Mat input) {
        Mat mask = new Mat();

        channels = new ArrayList<>();

        switch(color){
            case RED:
                if(threshold == -1){
                    threshold = 164;
                }

                Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2Lab);
                Imgproc.GaussianBlur(input,input,new Size(3,3),0);
                Core.split(input, channels);
                Imgproc.threshold(channels.get(1), mask, threshold, 255, Imgproc.THRESH_BINARY);
                break;
            case BLUE:
                if(threshold == -1){
                    threshold = 145;
                }

                Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2YUV);
                Imgproc.GaussianBlur(input,input,new Size(3,3),0);
                Core.split(input, channels);
                Imgproc.threshold(channels.get(1), mask, threshold, 255, Imgproc.THRESH_BINARY);
                break;
            case YELLOW:
                if(threshold == -1){
                    threshold = 95;
                }

                Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2YUV);
                Imgproc.GaussianBlur(input,input,new Size(3,3),0);
                Core.split(input, channels);
                Imgproc.threshold(channels.get(1), mask, threshold, 255, Imgproc.THRESH_BINARY_INV);
                break;
        }

        for(int i=0;i<channels.size();i++){
            channels.get(i).release();
        }

        input.release();

        return mask;

    }

    // RED FILTER

    public void leviRedFilter (Mat input, Mat mask, double threshold){


        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2Lab);
        Imgproc.GaussianBlur(input,input,new Size(3,3),0);
        Core.split(input, channels);
        Imgproc.threshold(channels.get(1), mask, threshold, 255, Imgproc.THRESH_BINARY);

        for(int i=0;i<channels.size();i++){
            channels.get(i).release();
        }
    }


    // BLUE FILTER

    public void leviBlueFilter (Mat input, Mat mask){
        List<Mat> channels = new ArrayList<>();

        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2Lab);
        Imgproc.GaussianBlur(input,input,new Size(3,3),0);
        Core.split(input, channels);
        Imgproc.threshold(channels.get(1), mask, 145, 255, Imgproc.THRESH_BINARY);

        for(int i=0;i<channels.size();i++){
            channels.get(i).release();
        }
    }

    public void leviBlueFilter (Mat input, Mat mask, double threshold){
        List<Mat> channels = new ArrayList<>();

        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2YUV);
        Imgproc.GaussianBlur(input,input,new Size(3,3),0);
        Core.split(input, channels);
        Imgproc.threshold(channels.get(1), mask, threshold, 255, Imgproc.THRESH_BINARY);

        for(int i=0;i<channels.size();i++){
            channels.get(i).release();
        }
    }
}