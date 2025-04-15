package com.diydrawing.ui.drawing.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.diydrawing.R
import com.diydrawing.ui.drawing.model.DrawingAction

@Composable
fun TopBar(
    progress: Float,
    onDrawingAction: (DrawingAction) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        RemoveLastPathButton(
            modifier = Modifier
                .padding(24.dp)
                .size(48.dp)
        ) {
            onDrawingAction(DrawingAction.RemoveLastPath)
        }
        DrawingProgressBar(
            modifier = Modifier.align(Alignment.Center),
            progress = progress
        )
    }
}

@Composable
private fun RemoveLastPathButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_return),
            contentDescription = "Undo",
            tint = Color.Black
        )
    }
}

@Composable
fun DrawingProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val animateProgress = animateFloatAsState(
        targetValue = progress,
        label = "DrawingProgressBar",
        animationSpec = tween(durationMillis = 300)
    )
    Box(
        modifier = modifier
            .width(160.dp)
            .height(16.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(fraction = animateProgress.value.coerceIn(0f, 1f))
                .background(Color.Green, RoundedCornerShape(8.dp))
        )
    }
}