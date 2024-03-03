package origami.filters

import org.opencv.core.Mat
import org.opencv.photo.Photo
import origami.Filter

class Stilyze : Filter {
    var sigma_s = 60f
    var sigma_r = 0.07f

    override fun apply(`in`: Mat): Mat {
        val dst = Mat()
        Photo.stylization(`in`, dst, sigma_s, sigma_r)
        return dst
    }
}
