package com.diydrawing.ui.drawing.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.diydrawing.ui.drawing.model.DrawingAction
import com.diydrawing.ui.drawing.model.DrawingUiState


@Composable
fun DrawingScreen(
    drawingUiState: DrawingUiState,
    onDrawingAction: (DrawingAction) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        DrawingContent(
            drawingUiState = drawingUiState,
            onDrawingAction = onDrawingAction,
            modifier = Modifier.align(Alignment.Center)
        )
        TopBar(
            progress = drawingUiState.progress.floatValue,
            onDrawingAction = onDrawingAction
        )
        val resources = LocalContext.current.resources
        LaunchedEffect(Unit) {
            onDrawingAction(DrawingAction.InitBitmap(resources))
        }
    }
}

@Composable
private fun DrawingContent(
    drawingUiState: DrawingUiState,
    onDrawingAction: (DrawingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        drawingUiState.initialImageBitmap.value?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                contentScale = ContentScale.FillWidth,
            )
        }
        DrawnImage(
            drawingUiState = drawingUiState,
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.Center),
        )
        DrawingInput(
            onDrawingAction = onDrawingAction,
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.Center)
        )
    }
}


@Composable
private fun DrawingInput(
    onDrawingAction: (DrawingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        onDrawingAction(DrawingAction.Start(offset))
                    },
                    onDragEnd = { onDrawingAction(DrawingAction.End) },
                    onDragCancel = { onDrawingAction(DrawingAction.End) },
                    onDrag = { change, _ ->
                        onDrawingAction(DrawingAction.MoveTo(change.position))
                    }
                )
            }
    )
}

@Composable
private fun DrawnImage(
    drawingUiState: DrawingUiState,
    modifier: Modifier = Modifier,
) {
    drawingUiState.drawingPathList.toList()
    drawingUiState.drawnImageBitmap.value?.let {
        Canvas(modifier = modifier) {
            drawImage(it)
        }
    }
}


@Preview
@Composable
private fun DrawingScreenPreview() {
    DrawingScreen(
        drawingUiState = DrawingUiState(),
        onDrawingAction = {}
    )
}