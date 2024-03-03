package origami.filters

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import origami.Filter

class Zoom : Filter {
    var zoomingFactor = 5
    var interpolation = Imgproc.INTER_LINEAR

    override fun apply(source: Mat): Mat {
        val destination = Mat(source.rows(), source.cols(), source.type())
        Imgproc.resize(source, destination, destination.size(), zoomingFactor.toDouble(), zoomingFactor.toDouble(), interpolation)
        return destination
    }
}
