package origami.filters

import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import origami.Filter
import origami.Origami

class Resize : Filter {
    var width = -1.0
    var height = -1.0
    var factor = -1.0

    override fun apply(frame: Mat): Mat {
        if (factor == -1.0 && height == -1.0 && width == -1.0) return frame
        val s = if (factor != -1.0) Size(frame.size().width * factor, frame.size().height * factor) else Size(width, height)
        val target = Mat()
        Imgproc.resize(frame, target, s)
        return target
    }
}
