package com.diydrawing.ui.drawing

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BitmapAnalyzer {

    suspend fun calculateColoredPixels(
        bitmap: Bitmap,
        color: Int,
    ): Int = withContext(Dispatchers.Default) {
        val width = bitmap.width
        val height = bitmap.height
        var coloredPixels = 0

        for (y in 0 until height) {
            for (x in 0 until width) {
                if (bitmap.getPixel(x, y) == color) {
                    coloredPixels++
                }
            }
        }
        coloredPixels
    }

    suspend fun calculateDrawingDifference(
        initialBitmap: Bitmap,
        initialColor: Int,
        totalColoredPixels: Int,
        drawnBitmap: Bitmap,
        drawnColor: Int,
    ): Float = withContext(Dispatchers.Default) {
        if (totalColoredPixels == 0) return@withContext 0f

        if (initialBitmap.width != drawnBitmap.width
            || initialBitmap.height != drawnBitmap.height
        ) {
            error("Bitmaps must be the same size")
        }

        val width = initialBitmap.width
        val height = initialBitmap.height
        var alignPixels = 0

        for (y in 0 until height) {
            for (x in 0 until width) {
                val initialPixel = initialBitmap.getPixel(x, y)
                val drawnPixel = drawnBitmap.getPixel(x, y)
                if (initialPixel == initialColor && drawnPixel == drawnColor) {
                    alignPixels++
                }
            }
        }

        alignPixels.toFloat() / totalColoredPixels
    }
}