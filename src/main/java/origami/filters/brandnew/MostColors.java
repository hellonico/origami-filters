package origami.filters.brandnew;

import org.opencv.core.*;
import origami.Filter;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.photo.Photo.illuminationChange;

public class MostColors implements Filter {

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    double coefficient = 0b11000000;

    @Override
    public Mat apply(Mat image) {
        // 16M colors -> 256 colors
        cvtColor(image, image, COLOR_BGR2HSV);

//        System.out.println((int)c);
        Scalar s = new Scalar(coefficient, coefficient, coefficient);
        Mat mask = new Mat(image.size(), image.type(), s);

        bitwise_and(image, mask, image);
//
        cvtColor(image, image, COLOR_HSV2BGR);

        return image;
    }
}

