package origami.filters

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import origami.Filter

class Thresh : Filter {
    var sensitivity = 100
    var maxVal = 255

    override fun apply(img: Mat): Mat {
        val threshed = Mat()
        Imgproc.threshold(img, threshed, sensitivity.toDouble(), maxVal.toDouble(), Imgproc.THRESH_BINARY)
        return threshed
    }
}
