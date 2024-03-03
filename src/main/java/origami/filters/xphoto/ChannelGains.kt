package origami.filters.xphoto

import org.opencv.core.Mat
import org.opencv.xphoto.Xphoto
import origami.Filter

class ChannelGains : Filter {
    var red = 1.0f
    var blue = 1.0f
    var green = 1.0f
    override fun apply(mat: Mat): Mat {
        Xphoto.applyChannelGains(mat, mat, blue, green, red)
        return mat
    }
}
