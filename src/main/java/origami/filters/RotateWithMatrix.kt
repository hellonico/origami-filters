package origami.filters

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc
import origami.Filter
import origami.utils.Utils

class RotateWithMatrix : Filter {
    @JvmField
    var rotationAngle = 10.0
    @JvmField
    var scale = 1.0
    var point = Point(50.0, 50.0)

    fun setPoint(point: String?) {
        val p = Utils.String_Point(point)
        this.point = p
    }

    fun getPoint(): String {
        return Utils.Point_String(point)
    }


    override fun apply(image: Mat): Mat {

        // Create a rotation matrix
        val rotationMatrix = Imgproc.getRotationMatrix2D(point, rotationAngle, scale)

        // Rotate the bubble and the text
        val rotatedImage = Mat()
        Imgproc.warpAffine(image, rotatedImage, rotationMatrix, image.size())
        return rotatedImage
    }
}
