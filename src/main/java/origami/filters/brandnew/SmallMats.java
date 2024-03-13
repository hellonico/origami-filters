package origami.filters.brandnew;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import origami.Filter;
import origami.FixedSizeList;

public class SmallMats implements Filter {

    public int getMainRows() {
        return mainRows;
    }

    public void setMainRows(int mainRows) {
        this.mainRows = mainRows;
    }

    public int getMainCols() {
        return mainCols;
    }

    public void setMainCols(int mainCols) {
        this.mainCols = mainCols;
    }

    public int mainRows = 4;
    public int mainCols = 3;

    FixedSizeList<Mat> cache = new FixedSizeList<Mat>(10);

    int counter = 0;

    public int max = 10000;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public Mat apply(Mat originalMat) {
        if(counter % max == 0) {
            cache.add(originalMat);
        }
        counter++;

        int smallRows = originalMat.height() / mainRows;
        int smallCols = originalMat.width() / mainCols;
        Size smallSize = new Size(smallCols, smallRows);

        Mat mainMat = new Mat(originalMat.size(), CvType.CV_8UC3, new Scalar(0, 0, 0));

        int matIndex = 0;

        for (int i = 0; i < mainRows; i++) {
            for (int j = 0; j < mainCols; j++) {
                Mat smallMat = mainMat.submat(i * smallRows, (i + 1) * smallRows, j * smallCols, (j + 1) * smallCols);

                Mat currentMat = cache.get(matIndex);

                Mat resizedMat = new Mat();
                Imgproc.resize(currentMat, resizedMat, smallSize);
                resizedMat.copyTo(smallMat, new Mat());

                // Move to the next mat in the list
                matIndex++;
                if (matIndex >= cache.size()) {
                    matIndex = 0; // Go back to the first element of the list
                }

            }
        }

        return mainMat;
    }
}
