package com.diydrawing.ui.drawing.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Path

@Stable
class DrawingPath(
    val id: Int,
    val path: Path,
) {

    override fun equals(other: Any?): Boolean {
        return if (other is DrawingPath) {
            this.id == other.id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}