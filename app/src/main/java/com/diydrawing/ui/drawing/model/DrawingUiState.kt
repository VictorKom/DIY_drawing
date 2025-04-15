package com.diydrawing.ui.drawing.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap

@Stable
class DrawingUiState {

    var initialBitmap: Bitmap? = null
    var initialImageBitmap: MutableState<ImageBitmap?> = mutableStateOf(null)
    val initialColor = -10058283
    var coloredPixels: Int? = null

    var drawnBitmap: Bitmap? = null
    var drawnImageBitmap: MutableState<ImageBitmap?> = mutableStateOf(null)
    val drawingColor = Color.rgb(252, 236, 169)

    var canvas: Canvas? = null
    val paint = Paint().apply {
        color = drawingColor
        style = Paint.Style.STROKE
        strokeWidth = 15f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
    }

    val drawingPathList = SnapshotStateList<DrawingPath>()
    val progress = mutableFloatStateOf(0f)
}