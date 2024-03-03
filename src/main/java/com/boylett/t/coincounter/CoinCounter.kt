package com.boylett.t.coincounter

import org.opencv.core.Mat
import org.opencv.core.RotatedRect
import org.opencv.imgproc.Imgproc
import origami.Filter
import origami.colors.HTML

class CoinCounter : Filter {
    @JvmField
    var last: List<RotatedRect> = ArrayList()
    var color = "white"
    private var ed = EllipseDetection.Builder().build()

    override fun apply(image: Mat): Mat {
        val rects = find(image)
        last = rects
        return draw(image, rects)
    }

    fun draw(image: Mat, rects: List<RotatedRect>): Mat {
        for (rect in rects) {
            Imgproc.ellipse(image, rect, HTML.toScalar(color), -1)
        }
        return image
    }

    fun find(image: Mat): List<RotatedRect> {
        return ed.detect(image.clone())
    }
}