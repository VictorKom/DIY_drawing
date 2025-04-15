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
import com.diydrawing.utils.throttleLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch

class DrawingViewModel : ViewModel() {

    val drawingUiState = DrawingUiState()
    private val idGenerator = IdGenerator()
    private val bitmapAnalyzer = BitmapAnalyzer()

    init {
        observePathForDrawing()
        observePathForProgressCalculation()
    }

    private fun observePathForDrawing() {
        snapshotFlow {
            drawingUiState.drawingPathList.toList()
        }.onEach {
            drawPathList()
        }.launchIn(viewModelScope)
    }

    private fun observePathForProgressCalculation() {
        snapshotFlow {
            drawingUiState.drawingPathList.toList()
        }
            .throttleLatest(300L)
            .onEach {
                val initialBitmap = drawingUiState.initialBitmap ?: return@onEach
                val drawnBitmap = drawingUiState.drawnBitmap ?: return@onEach
                val coloredPixels = drawingUiState.coloredPixels ?: return@onEach
                val progress = bitmapAnalyzer.calculateDrawingDifference(
                    initialBitmap,
                    drawingUiState.initialColor,
                    coloredPixels,
                    drawnBitmap,
                    drawingUiState.drawingColor
                )
                drawingUiState.progress.floatValue = progress
            }
            .retryWhen { cause, _ ->
                cause is IllegalStateException
            }
            .launchIn(viewModelScope)
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
        viewModelScope.launch {
            val initialColoredPixels = bitmapAnalyzer.calculateColoredPixels(
                initialBitmap,
                initialColor
            )
            drawingUiState.coloredPixels = initialColoredPixels
        }

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
}