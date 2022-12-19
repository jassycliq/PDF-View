package com.jassycliq.pdfview.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap
import com.jassycliq.pdfview.feature.DarkMode

/**
 * A data class that represents the UI state of the PDF view.
 *
 * @param pdfBitmap The image bitmap of the PDF.
 * @param darkMode The dark mode state of the PDF view.
 */
@Immutable
internal data class UiState(
    val pdfBitmap: ImageBitmap? = null,
    val darkMode: DarkMode,
)
