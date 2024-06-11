package origami.filters.brandnew

import org.opencv.core.Core
import org.opencv.core.Core.*
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.photo.Photo
import org.opencv.photo.TonemapReinhard
import origami.Filter

class FakeHDR : Filter {
    var tonemapper: TonemapReinhard = Photo.createTonemapReinhard()

    var gamma: Float
        get() = tonemapper.gamma
        set(gamma) {
            tonemapper.gamma = gamma
        }

    var intensity: Float
        get() = tonemapper.intensity
        set(intensity) {
            tonemapper.intensity = intensity
        }

    var colorAdaptation: Float
        get() = tonemapper.colorAdaptation
        set(colorAdaptation) {
            tonemapper.colorAdaptation = colorAdaptation
        }

    var lightAdaptation: Float
        get() = tonemapper.lightAdaptation
        set(lightAdaptation) {
            tonemapper.lightAdaptation = lightAdaptation
        }

    override fun apply(inputImage: Mat): Mat {
        // Convert the image to the HLS color space
        val hlsImage = Mat()

        // Convert the input channel to floating point type and normalize it
        val floatChannel = Mat()
        inputImage.convertTo(floatChannel, CvType.CV_32F)
        normalize(floatChannel, floatChannel, 0.0, 1.0, NORM_MINMAX, CvType.CV_32F)

        // Apply tone mapping using TonemapDurand
        val tonemappedChannel = Mat()
        tonemapper.process(floatChannel, tonemappedChannel)

        // Convert the tonemapped channel back to the original range
        normalize(tonemappedChannel, tonemappedChannel, 0.0, 255.0, NORM_MINMAX, CvType.CV_8U)

        return tonemappedChannel
    }
}
