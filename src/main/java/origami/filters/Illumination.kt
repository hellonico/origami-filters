package origami.filters

import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.photo.Photo
import origami.Filter

class Illumination : Filter {
    var alpha = 3f
    var beta = 0.4f
    override fun apply(`in`: Mat): Mat {
        val dst = Mat()
        val mask = Mat.ones(`in`.size(), CvType.CV_8UC1)
        Photo.illuminationChange(`in`, mask, dst, alpha, beta)
        return dst
    }
}
