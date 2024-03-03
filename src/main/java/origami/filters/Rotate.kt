package origami.filters

import org.opencv.core.Core
import org.opencv.core.Mat
import origami.Filter

abstract class Rotate : Filter {
    protected var rotateAngle = Core.ROTATE_90_CLOCKWISE

    class Rotate90 : Rotate() {
        init {
            rotateAngle = Core.ROTATE_90_CLOCKWISE
        }
    }

    class Rotate180 : Rotate() {
        init {
            rotateAngle = Core.ROTATE_180
        }
    }

    class Rotate270 : Rotate() {
        init {
            rotateAngle = Core.ROTATE_90_COUNTERCLOCKWISE
        }
    }

    override fun apply(`in`: Mat): Mat {
        val r = Mat()
        Core.rotate(`in`, r, rotateAngle)
        return r
    }
}
