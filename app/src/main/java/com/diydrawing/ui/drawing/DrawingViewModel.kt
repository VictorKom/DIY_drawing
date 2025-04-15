package com.diydrawing.ui.drawing

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diydrawing.R
import com.diydrawing.ui.drawing.model.DrawingAction
import com.diydrawing.ui.drawing.model.DrawingPath
import com.diydrawing.ui.drawing.model.DrawingUiState
import com.diydrawing.utils.IdGenerator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DrawingViewModel : ViewModel() {

    val drawingUiState = DrawingUiState()
    private val idGenerator = IdGenerator()

    init {
        snapshotFlow {
            drawingUiState.drawingPathList.toList()
        }.onEach {
            drawPathList()
        }.launchIn(viewModelScope)
    }

    fun onDrawingAction(drawingAction: DrawingAction) {
        when (drawingAction) {
            is DrawingAction.InitBitmap -> initDrawingBitmap(drawingAction.resources)
            is DrawingAction.Start -> createNewPath(drawingAction.startPoint)
            is DrawingAction.MoveTo -> moveToCurrentPath(drawingAction.point)
            is DrawingAction.RemoveLastPath -> removeLastPath()
            DrawingAction.End -> Unit
        }
    }

    private fun initDrawingBitmap(resources: Resources) = with(drawingUiState) {
        val initialBitmap =
            (resources.getDrawable(R.drawable.circus_image, null) as BitmapDrawable).bitmap
        this.initialBitmap = initialBitmap
        initialImageBitmap.value = initialBitmap.asImageBitmap()

        val bitmap = Bitmap.createBitmap(
            initialBitmap.width,
            initialBitmap.height,
            Bitmap.Config.ARGB_8888
        )
        this.drawnBitmap = bitmap
        drawnImageBitmap.value = bitmap.asImageBitmap()
        canvas = Canvas(bitmap)
    }

    private fun createNewPath(startPoint: Offset) {
        drawingUiState.drawingPathList.add(
            DrawingPath(
                id = idGenerator.generate(),
                path = Path().also {
                    it.moveTo(startPoint.x, startPoint.y)
                }
            )
        )
    }

    private fun moveToCurrentPath(point: Offset) {
        val lastIndex = drawingUiState.drawingPathList.lastIndex
        drawingUiState.drawingPathList.getOrNull(lastIndex)?.let {
            it.path.lineTo(point.x, point.y)
            drawingUiState.drawingPathList.set(
                lastIndex,
                DrawingPath(
                    id = idGenerator.generate(),
                    path = it.path
                )
            )
        }
    }

    private fun removeLastPath() {
        drawingUiState.drawingPathList.removeLastOrNull()
    }

    private fun drawPathList() = with(drawingUiState) {
        canvas?.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR)
        drawingPathList.forEach {
            canvas?.drawPath(it.path.asAndroidPath(), paint)
        }
    }

//    fun calculateDrawingDifference(initial: Bitmap, drawn: Bitmap): Float {
//        if (initial.width != drawn.width || initial.height != drawn.height) {
//            throw IllegalArgumentException("Bitmaps must be the same size")
//        }
//
//        val width = initial.width
//        val height = initial.height
//        val totalPixels = width * height
//        var alignPixels = 0
//
//        for (y in 0 until height) {
//            for (x in 0 until width) {
//                if (initial.getPixel(x, y) != bitmap2.getPixel(x, y)) {
//                    alignPixels++
//                }
//            }
//        }
//
//        // Return percentage of difference (0.0 = identical, 1.0 = completely different)
//        return differentPixels.toFloat() / totalPixels
//    }
}