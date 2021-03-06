package com.isaac.examples;

import com.isaac.models.FusionEnhance;
import com.isaac.utils.ImShow;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import origami.Origami;

public class FusionEnhanceExample {

    static {
        Origami.init();
    }

    private static final int level = 5;

    public static void main (String[] args) {
        String imgPath = "src/main/resources/underwater_images/underwater_scene.jpg";
        Mat image = Imgcodecs.imread(imgPath, Imgcodecs.IMREAD_COLOR);
        new ImShow("original").showImage(image);
        Mat fusion = FusionEnhance.enhance(image, level);
        fusion.convertTo(fusion, CvType.CV_8UC1);
        new ImShow("fusion").showImage(fusion);
    }
}
