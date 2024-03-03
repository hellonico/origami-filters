package origami.filters

import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import origami.Filter

object HueSaturationValue {
    fun filter(marcel: Mat, cs1: Int, cs2: Int, vararg values: Double): Mat {
        val m = Mat(1, 3, CvType.CV_64F)
        m.put(0, 0, *values)
        if (cs1 != -1) Imgproc.cvtColor(marcel, marcel, cs1)
        Core.multiply(marcel, m, marcel)
        if (cs2 != -1) Imgproc.cvtColor(marcel, marcel, cs2)
        return marcel
    }

    class Pink : Filter {
        override fun apply(`in`: Mat): Mat {
            filter(`in`, Imgproc.COLOR_BGR2HSV, Imgproc.COLOR_HSV2BGR, 0.2, 1.0, 1.0)
            return `in`
        }
    }

    class Nashville : Filter {
        override fun apply(marcel: Mat): Mat {
            filter(marcel, Imgproc.COLOR_BGR2HSV, Imgproc.COLOR_HSV2BGR, 1.0, 1.5, 1.0)
            marcel.convertTo(marcel, -1, 1.5, -30.0)
            return marcel
        }
    }

    class Gotham : Filter {
        override fun apply(marcel: Mat): Mat {
            filter(marcel, Imgproc.COLOR_BGR2HSV, Imgproc.COLOR_HSV2BGR, 1.0, 0.2, 1.0)
            marcel.convertTo(marcel, -1, 1.3, -20.0)
            return marcel
        }
    }

    class Lomo : Filter {
        override fun apply(`in`: Mat): Mat {
            filter(`in`, -1, -1, 1.0, 1.33, 1.33)
            return `in`
        }
    }
}