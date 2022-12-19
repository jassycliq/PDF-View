package com.jassycliq.pdfview.ui

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.AbstractComposeView

class PdfViewXml @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AbstractComposeView(context, attrs, defStyle) {

    private var pdfPath = mutableStateOf("")

    var filePath: String
        get() = pdfPath.value
        set(value) {
            pdfPath.value = value
        }

    @Composable
    override fun Content() {
        val filePath by rememberSaveable { pdfPath }
        PdfView(filePath = filePath)
    }
}
