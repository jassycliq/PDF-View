package com.jassycliq.pdfview.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.layout
import com.jassycliq.pdfview.ZoomState
import com.jassycliq.pdfview.rememberZoomState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun ZoomView(
    modifier: Modifier = Modifier,
    Content: @Composable (modifier: Modifier) -> Unit,
) {
    val zoomState: ZoomState = rememberZoomState()
    val scrollState: ScrollState = rememberScrollState()
    val scope: CoroutineScope = rememberCoroutineScope()

    BoxWithConstraints {

        var childWidth by remember { mutableStateOf(0) }
        var childHeight by remember { mutableStateOf(0) }

        LaunchedEffect(childHeight, childWidth, zoomState.scale) {
            zoomState.updateBounds(
                maxX = (childWidth * zoomState.scale - constraints.maxWidth).coerceAtLeast(0f) / 2f,
                maxY = (childHeight * zoomState.scale - constraints.maxHeight).coerceAtLeast(0f) / 2f,
            )
        }

        Content(modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDrag(
                    onDrag = { change, dragAmount ->
                        if (change.positionChange() != Offset.Zero) change.consume()
                        if (zoomState.zooming) {
                            scope.launch {
                                zoomState.drag(dragAmount)
                                zoomState.addPosition(change.uptimeMillis, change.position)
                            }
                        }
                    },
                    onDragEnd = { if (zoomState.zooming) scope.launch { zoomState.dragEnd() } },
                    onDragCancel = { zoomState.resetTracking() },
                )
            }
            .then(Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scope.launch {
                            zoomState.animateScaleTo(if (zoomState.scale > 3f) zoomState.minScale else zoomState.scale * 2)
                        }
                    }
                )
            })
            .transformable(
                state = rememberTransformableState { zoomChange, _, _ ->
                    scope.launch {
                        zoomState.onZoomChange(zoomChange)
                    }
                }
            )
            .layout { measurable, constraints ->
                with(measurable.measure(constraints = constraints)) {
                    childHeight = height
                    childWidth = width
                    layout(
                        width = constraints.maxWidth,
                        height = constraints.maxHeight,
                    ) {
                        placeRelativeWithLayer(
                            x = ((constraints.maxWidth - width) / 2),
                            y = ((constraints.maxHeight - height) / 2),
                        ) {
                            scaleX = zoomState.scale
                            scaleY = zoomState.scale
                            translationX = zoomState.translateX
                            translationY = zoomState.translateY
                        }
                    }
                }
            }
            .verticalScroll(scrollState)
        )
    }
}

/**
 * Detects drag gestures and calls [onDrag] when a drag is detected.
 *
 * @param onDrag the callback to be invoked when a drag is detected
 * @param onDragEnd the callback to be invoked when a drag ends
 * @param onDragCancel the callback to be invoked when a drag is cancelled
 */
private suspend fun PointerInputScope.detectDrag(
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit,
) {
    forEachGesture {
        awaitPointerEventScope {
            val down = awaitFirstDown(requireUnconsumed = false)
            var drag: PointerInputChange?
            do {
                drag = awaitTouchSlopOrCancellation(down.id, onDrag)
            } while (drag != null && drag.isConsumed.not())
            drag?.let {
                onDragStart(drag.position)
                when (drag(drag.id) { onDrag(it, it.positionChange()) }) {
                    true -> onDragEnd()
                    false -> onDragCancel()
                }
            }
        }
    }
}
