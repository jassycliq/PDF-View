package com.jassycliq.pdfview.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.jassycliq.pdfview.extension.createPdfBitmap
import com.jassycliq.pdfview.feature.DarkMode
import com.jassycliq.pdfview.feature.DarkMode.Disabled
import com.jassycliq.pdfview.feature.DarkMode.Enabled
import com.jassycliq.pdfview.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

@Composable
fun PdfView(
    modifier: Modifier = Modifier,
    filePath: String = "",
    darkMode: Boolean = false,
) = InternalPdfView(
    modifier = modifier,
    filePath = filePath,
    darkMode = if (darkMode) Enabled else Disabled,
)

@Composable
internal fun InternalPdfView(
    modifier: Modifier,
    filePath: String,
    darkMode: DarkMode,
    uiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(UiState(darkMode = darkMode)),
) {
    val uiState = uiStateFlow.collectAsState()

    LaunchedEffect(filePath) {
        File(filePath).apply {
            if (exists()) {
                uiStateFlow.update {
                    it.copy(pdfBitmap = createPdfBitmap().asImageBitmap())
                }
            }
        }
    }

    ZoomView(
        modifier = modifier,
    ) { childModifier ->
        when (val pdfBitmap = uiState.value.pdfBitmap) {
            null -> Unit // TODO: Display some sort of view as an error?
            else ->
                Image(
                    bitmap = pdfBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    colorFilter = uiState.value.darkMode.colorFilter,
                    modifier = childModifier,
                )
        }
    }
}
