package origami.filters;

public class Level implements Filter {

    int n = 100;

public Mat apply(Mat im) {
                Size sz = im.size();
                for (int i = 0; i < sz.height; i++) {
                    for (int j = 0; j < sz.width; j++) {
                        double[] pixcel = im.get(i, j);
                        pixcel[0] = ((int) pixcel[0] / n) * n + n / 2;
                        pixcel[1] = ((int) pixcel[1] / n) * n + n / 2;
                        pixcel[2] = ((int) pixcel[2] / n) * n + n / 2;
                        im.put(i, j, pixcel);
                    }
                }
                return im;
            }
}
