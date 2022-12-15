package com.jassycliq.pdfview

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberPdfViewState(
    scaleRange: ClosedRange<Float> = 1f..Float.MAX_VALUE,
): PdfViewState = rememberSaveable(
    saver = PdfViewState.Saver
) {
    PdfViewState(
        minScale = scaleRange.start,
        maxScale = scaleRange.endInclusive,
    )
}

@Stable
class PdfViewState(
    @FloatRange(from = 0.0) val minScale: Float = 1f,
    @FloatRange(from = 0.0) val maxScale: Float = Float.MAX_VALUE,
    @FloatRange(from = 0.0) initialTranslateX: Float = 0f,
    @FloatRange(from = 0.0) initialTranslateY: Float = 0f,
    @FloatRange(from = 0.0) initialScale: Float = minScale,
) {
    private val velocityTracker = VelocityTracker()
    private val _translateY = Animatable(initialTranslateY)
    private val _translateX = Animatable(initialTranslateX)
    private val _scale = Animatable(initialScale)

    init {
        require(minScale < maxScale) { "minScale must be < maxScale" }
    }

    @get:FloatRange(from = 0.0)
    val scale: Float
        get() = _scale.value

    @get:FloatRange(from = 0.0)
    val translateY: Float
        get() = _translateY.value

    @get:FloatRange(from = 0.0)
    val translateX: Float
        get() = _translateX.value

    internal val zooming: Boolean
        get() = scale > minScale

    private suspend fun snapScaleTo(scale: Float) =
        _scale.snapTo(scale.coerceIn(minimumValue = minScale, maximumValue = maxScale))

    suspend fun animateScaleTo(
        scale: Float,
        animationSpec: AnimationSpec<Float> = spring(),
        initialVelocity: Float = 0f,
    ) = _scale.animateTo(
        targetValue = scale.coerceIn(minimumValue = minScale, maximumValue = maxScale),
        animationSpec = animationSpec,
        initialVelocity = initialVelocity,
    )

    private suspend fun fling(velocity: Offset) = coroutineScope {
        launch { _translateX.animateDecay(velocity.x, exponentialDecay()) }
        launch { _translateY.animateDecay(velocity.y, exponentialDecay()) }
    }

    internal suspend fun drag(dragDistance: Offset) = coroutineScope {
        launch { _translateX.snapTo((_translateX.value + dragDistance.x)) }
        launch { _translateY.snapTo((_translateY.value + dragDistance.y)) }
    }

    internal suspend fun dragEnd(): Unit = with(velocityTracker.calculateVelocity()) {
        fling(Offset(x, y))
    }

    internal suspend fun updateBounds(maxX: Float, maxY: Float) = coroutineScope {
        _translateY.updateBounds(-maxY, maxY)
        _translateX.updateBounds(-maxX, maxX)
    }

    internal suspend fun onZoomChange(zoomChange: Float) = snapScaleTo(scale * zoomChange)

    internal fun addPosition(timeMillis: Long, position: Offset) =
        velocityTracker.addPosition(timeMillis = timeMillis, position = position)


    internal fun resetTracking() = velocityTracker.resetTracking()

    internal fun isHorizontalDragFinished(dragDistance: Offset): Boolean {
        val lowerBounds = _translateX.lowerBound ?: return false
        val upperBounds = _translateX.upperBound ?: return false
        if (lowerBounds == 0f && upperBounds == 0f) return true

        val newPosition = _translateX.value + dragDistance.x

        if (newPosition <= lowerBounds) {
            return true
        }

        if (newPosition >= upperBounds) {
            return true
        }
        return false
    }

    override fun toString(): String = "PdfViewState(" +
            "minScale=$minScale, " +
            "maxScale=$maxScale, " +
            "translateY=$translateY" +
            "translateX=$translateX" +
            "scale=$scale" +
            ")"

    companion object {
        val Saver: Saver<PdfViewState, *> = listSaver(
            save = {
                listOf(
                    it.translateX,
                    it.translateY,
                    it.scale,
                    it.minScale,
                    it.maxScale,
                )
            },
            restore = {
                PdfViewState(
                    initialTranslateX = it[0],
                    initialTranslateY = it[1],
                    initialScale = it[2],
                    minScale = it[3],
                    maxScale = it[4],
                )
            }
        )
    }
}