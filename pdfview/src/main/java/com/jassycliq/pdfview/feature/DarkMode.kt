package com.jassycliq.pdfview.feature

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix

/**
 * A sealed class that represents the dark mode state of the PDF.
 * Should only be used on Black and White PDFs as it uses a color matrix to
 * invert the colors.
 */
sealed class DarkMode(val colorFilter: ColorFilter) {
    object Disabled : DarkMode(ColorFilter.colorMatrix(NON_INVERT))
    object Enabled : DarkMode(ColorFilter.colorMatrix(INVERT))
}

/**
 * A color matrix that inverts the colors of the PDF.
 */
private val INVERT = ColorMatrix(
    floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    )
)

/**
 * A color matrix that does not invert the colors of the PDF.
 */
private val NON_INVERT = ColorMatrix(
    floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )
)
