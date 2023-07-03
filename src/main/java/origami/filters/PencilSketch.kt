package origami.filters

import org.opencv.core.Mat
import org.opencv.photo.Photo
import origami.Filter

class PencilSketch : Filter {
    var sigma_s = 60f
    var sigma_r = 0.07f
    var shade_factor = 0.05f
    var gray = false

    override fun apply(`in`: Mat): Mat {
        val dst = Mat()
        val dst2 = Mat()
        Photo.pencilSketch(`in`, dst, dst2, sigma_s, sigma_r, shade_factor)
        return if (gray) dst else dst2
    }
}
