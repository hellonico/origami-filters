package origami.filters

import org.opencv.core.Mat
import origami.Filter

class Level : Filter {
    var n = 100
    override fun apply(im: Mat): Mat {
        val sz = im.size()
        var i = 0
        while (i < sz.height) {
            var j = 0
            while (j < sz.width) {
                val pixel = im[i, j]
                pixel[0] = (pixel[0].toInt() / n * n + n / 2).toDouble()
                pixel[1] = (pixel[1].toInt() / n * n + n / 2).toDouble()
                pixel[2] = (pixel[2].toInt() / n * n + n / 2).toDouble()
                im.put(i, j, *pixel)
                j++
            }
            i++
        }
        return im
    }
}
