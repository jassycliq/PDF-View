package com.jassycliq.pdfview

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.AbstractComposeView
import java.io.File

// TODO: Need to work on this, currently doesn't work as intended (at all?)
class PdfViewXml @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AbstractComposeView(context, attrs, defStyle) {

    private val pdfFile = mutableStateOf<File?>(null)

    var pdf: File?
        get() = pdfFile.value
        set(value) {
            pdfFile.value = value
        }

    @Composable
    override fun Content() {
        PdfView(state = rememberPdfViewState(pdf))
    }
}
