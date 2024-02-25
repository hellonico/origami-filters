package origami.filters.brandnew;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.photo.Photo;
import org.opencv.photo.TonemapReinhard;
import origami.Filter;

public class FakeHDR implements Filter {

    TonemapReinhard tonemapper = Photo.createTonemapReinhard();

    public float getGamma() {
        return this.tonemapper.getGamma();
    }

    public void setGamma(float gamma) {
        this.tonemapper.setGamma(gamma);
    }

    public float getIntensity() {
        return this.tonemapper.getIntensity();
    }

    public void setIntensity(float intensity) {
        this.tonemapper.setIntensity(intensity);
    }

    public float getColorAdaptation() {
        return this.tonemapper.getColorAdaptation();
    }

    public void setColorAdaptation(float colorAdaptation) {
        this.tonemapper.setColorAdaptation(colorAdaptation);
    }

    public float getLightAdaptation() {
        return this.tonemapper.getLightAdaptation();
    }

    public void setLightAdaptation(float lightAdaptation) {
        this.tonemapper.setLightAdaptation(lightAdaptation);
    }

    @Override
    public Mat apply(Mat inputImage) {
        // Convert the image to the HLS color space
        Mat hlsImage = new Mat();
//        Imgproc.cvtColor(inputImage, hlsImage, Imgproc.COLOR_BGR2HLS);

        // Convert the input channel to floating point type and normalize it
        Mat floatChannel = new Mat();
        inputImage.convertTo(floatChannel, CvType.CV_32F);
        Core.normalize(floatChannel, floatChannel, 0, 1, Core.NORM_MINMAX, CvType.CV_32F);

        // Apply tone mapping using TonemapDurand
        Mat tonemappedChannel = new Mat();
        this.tonemapper.process(floatChannel, tonemappedChannel);

        // Convert the tonemapped channel back to the original range
        Core.normalize(tonemappedChannel, tonemappedChannel, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);

        return tonemappedChannel;
    }

}
