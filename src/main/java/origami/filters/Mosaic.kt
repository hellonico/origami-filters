package origami.filters

import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import origami.Filter

class Mosaic : Filter {
    var factor = 0.1

    override fun apply(mat: Mat): Mat {
        val im = mat.clone()
        Imgproc.resize(im, im, Size(), factor, factor, Imgproc.INTER_NEAREST)
        Imgproc.resize(im, im, Size(), 1 / factor, 1 / factor, Imgproc.INTER_NEAREST)
        return im
    }
}
