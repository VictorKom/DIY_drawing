package com.diydrawing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.diydrawing.ui.drawing.compose.DrawingScreen
import com.diydrawing.ui.drawing.DrawingViewModel
import com.diydrawing.ui.theme.DIYDrawingTheme

class MainActivity : ComponentActivity() {

    private val drawingViewModel by viewModels<DrawingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DIYDrawingTheme {
                DrawingScreen(
                    drawingUiState = drawingViewModel.drawingUiState,
                    onDrawingAction = drawingViewModel::onDrawingAction,
                )
            }
        }
    }
}