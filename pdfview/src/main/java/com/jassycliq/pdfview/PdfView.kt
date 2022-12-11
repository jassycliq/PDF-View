package com.jassycliq.pdfview

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import java.io.File

@Composable
fun PdfView(
    modifier: Modifier = Modifier,
    file: File? = null,
    vm: PdfViewModel = viewModel(factory = PdfViewModelFactory(file)),
) {
    val pdfState: PdfState by vm.uiState.collectAsState()

    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableStateOf(1f) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    // TODO: Possibly display some sort of view as an error?
    pdfState.pdf?.let {
        LazyColumn(
            modifier = modifier
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, gestureZoom, _ ->
                        val oldScale = zoom
                        val newScale = (zoom * gestureZoom).coerceIn(1f, 3f)
                        zoom = newScale

                        // For natural zooming and rotating, the centroid of the gesture should
                        // be the fixed point where zooming and rotating occurs.
                        // We compute where the centroid was (in the pre-transformed coordinate
                        // space), and then compute where it will be after this delta.
                        // We then compute what the new offset should be to keep the centroid
                        // visually stationary for rotating and zooming.
                        val newOffset = ((offset + centroid / oldScale) - (centroid / newScale + pan / oldScale))

                        // TODO: Need to find a better way to limit panning to size of PDF
                        val maxX = (size.width * (zoom - 1) / 2f)
                        val maxY = (size.height * (zoom - 1) / 2f)

                        offset = Offset(
                            x = newOffset.x.coerceIn(0f..maxX),
                            y = newOffset.y.coerceIn(0f..maxY)
                        )
                    }
                }
                .onSizeChanged { size = it }
                .graphicsLayer {
                    translationX = -offset.x * zoom
                    translationY = -offset.y * zoom
                    scaleX = zoom
                    scaleY = zoom
                    transformOrigin = TransformOrigin(0f, 0f)
                }
        ) {
            item {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}
