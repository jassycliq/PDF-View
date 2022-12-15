package com.jassycliq.pdfview

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun PdfView(
    state: PdfViewState,
    modifier: Modifier = Modifier,
    filePath: String = "",
    scope: CoroutineScope = rememberCoroutineScope(),
    uiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(UiState())
) {
    val uiState = uiStateFlow.collectAsState()

    File(filePath).apply {
        if (exists()) {
            val isLowRamDevice = (LocalContext.current.getSystemService(ACTIVITY_SERVICE) as ActivityManager).isLowRamDevice
            uiStateFlow.update {
                it.copy(pdf = createPdfBitmap(isLowRamDevice).asImageBitmap())
            }
        }
    }

    BoxWithConstraints {

        var childWidth by remember { mutableStateOf(0) }
        var childHeight by remember { mutableStateOf(0) }

        LaunchedEffect(childHeight, childWidth, state.scale) {
            state.updateBounds(
                maxX = (childWidth * state.scale - constraints.maxWidth).coerceAtLeast(0f) / 2f,
                maxY = (childHeight * state.scale - constraints.maxHeight).coerceAtLeast(0f) / 2f,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDrag(
                        onDrag = { change, dragAmount ->
                            if (state.isHorizontalDragFinished(dragAmount * state.scale).not()) {
                                if (change.positionChange() != Offset.Zero) change.consume()
                            }
                            if (state.zooming) {
                                scope.launch {
                                    state.drag(dragAmount)
                                    state.addPosition(change.uptimeMillis, change.position)
                                }
                            }
                        },
                        onDragEnd = { if (state.zooming) scope.launch { state.dragEnd() } },
                        onDragCancel = { state.resetTracking() },
                    )
                }
                .then(Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            scope.launch {
                                state.animateScaleTo(if (state.scale > 3f) state.minScale else state.scale * 2)
                            }
                        }
                    )
                })
                .transformable(
                    state = rememberTransformableState { zoomChange, _, _ ->
                        scope.launch {
                            state.onZoomChange(zoomChange)
                        }
                    }
                )
                .layout { measurable, constraints ->
                    with(measurable.measure(constraints = constraints)) {
                        childHeight = height
                        childWidth = width
                        layout(
                            width = constraints.maxWidth,
                            height = constraints.maxHeight
                        ) {
                            placeRelativeWithLayer(
                                (constraints.maxWidth - width) / 2,
                                (constraints.maxHeight - height) / 2
                            ) {
                                scaleX = state.scale
                                scaleY = state.scale
                                translationX = state.translateX
                                translationY = state.translateY
                            }
                        }
                    }
                }
                .verticalScroll(rememberScrollState())

        ) {
            when (val pdf = uiState.value.pdf) {
                null -> Unit // TODO: Display some sort of view as an error?
                else ->
                    Image(
                        bitmap = pdf,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = modifier
                            .fillMaxWidth(),
                    )
            }
        }
    }
}

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
