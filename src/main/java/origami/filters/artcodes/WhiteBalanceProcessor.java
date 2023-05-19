package origami.artcodes;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import origami.Filter;

import java.util.ArrayList;
import java.util.List;

public class WhiteBalanceProcessor implements Filter {

    protected MatOfInt[] channels = null;
    protected Mat[] histograms = null;
    protected Mat emptyMatMask = null;
    protected MatOfInt size = null;
    protected MatOfFloat range = null;

    private void setup() {
        channels = new MatOfInt[] { new MatOfInt(0), new MatOfInt(1), new MatOfInt(2), new MatOfInt(3) };
        histograms = new Mat[] { new Mat(), new Mat(), new Mat(), new Mat() };
        emptyMatMask = new Mat();
        size = new MatOfInt(256);
        range = new MatOfFloat(0, 256);
    }

    @Override
    public Mat apply(Mat image) {
        if(this.histograms == null) {
            this.setup();
        }/*  w  ww.  j  av a  2s  .c  o  m*/

        List<Mat> listOfMat = new ArrayList<>();
        listOfMat.add(image);

        // create a histogram for each channel:
        // (oddly it seems ~10x faster to do 3 channels separately rather than all 3 in one calcHist call)
        for (int channel = 0; channel < image.channels(); ++channel) {
            Imgproc.calcHist(listOfMat, channels[channel], emptyMatMask, histograms[channel], size, range);
        }

        float[] a = new float[image.channels()];
        float[] b = new float[image.channels()];

        final int desiredHistogramBufferSize = histograms[0].rows() * histograms[0].cols() * histograms[0].channels();
        float[] pixelHistogramBuffer = new float[desiredHistogramBufferSize];

        // get the values to remap the histograms:
        for (int channel = 0; channel < image.channels(); ++channel) {
            histograms[channel].get(0, 0, pixelHistogramBuffer);
            getHistogramRemap(pixelHistogramBuffer, desiredHistogramBufferSize, image.total(), a, channel, b,
                    channel);
        }

        // Use a Look Up Table to re-map values
        // (it's a lot faster to workout and save what the 256 possible values transform into
        // than to do the math image.cols*rows times)

        Mat lut = new Mat(1, 256, CvType.CV_8UC3);

        final int lutSize = lut.cols() * lut.rows() * lut.channels();
        int lutIndex = -1;
        byte[] lutBufferArray = new byte[lutSize];
        for (int i = 0; i < 256; ++i) {
            for (int channel = 0; channel < image.channels(); ++channel) {
                lutBufferArray[++lutIndex] = (byte) Math.min(Math.max(a[channel] * ((i) - b[channel]), 0), 255);
            }
        }
        lut.put(0, 0, lutBufferArray);
        Core.LUT(image, lut, image);
        return image;
    }

    private static void getHistogramRemap(float[] histogram, int size, long total, float[] resultA,
                                          int resultAIndex, float[] resultB, int resultBIndex) {
        if (total == -1) {
            total = 0;
            for (int i = 0; i < size; ++i) {
                total += histogram[i];
            }
        }

        final float p5 = total * 0.05f, p95 = total * 0.95f;
        resultB[resultBIndex] = resultA[resultAIndex] = -1;
        int count = 0;

        for (int i = 0; i < size; ++i) {
            count += histogram[i];
            if (resultB[resultBIndex] == -1 && count >= p5) {
                resultB[resultBIndex] = i;
            } else if (count >= p95) {
                resultA[resultAIndex] = 255f / (i - resultB[resultBIndex]);
                break;
            }
        }
    }

}
