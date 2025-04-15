package com.diydrawing.ui.drawing.model

import android.content.res.Resources
import androidx.compose.ui.geometry.Offset

sealed class DrawingAction {

    data class InitBitmap(val resources: Resources) : DrawingAction()
    data class Start(val startPoint: Offset) : DrawingAction()
    data class MoveTo(val point: Offset) : DrawingAction()
    data object End : DrawingAction()

    data object RemoveLastPath : DrawingAction()
}