package com.jassycliq.pdfviewexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.jassycliq.pdfview.PdfView
import com.jassycliq.pdfview.rememberPdfViewState
import com.jassycliq.pdfviewexample.ui.theme.PDFViewTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    private var filePath = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            while(true) {
                renderDemoPdf()
                delay(10000)
                renderBlankPdf()
                delay(10000)
            }
        }

        setContent {
            PDFViewTheme {
                val pdfPath by rememberSaveable { filePath }

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    PdfView(filePath = pdfPath, state = rememberPdfViewState())
                }
            }
        }
    }

    private fun renderBlankPdf() =
        applicationContext.resources.openRawResource(R.raw.blank).use { input ->
            File(cacheDir, "blank.pdf").run {
                FileOutputStream(this).use { output ->
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
                filePath.value = path
            }
        }

    private fun renderDemoPdf() =
        applicationContext.resources.openRawResource(R.raw.demo).use { input ->
            File(cacheDir, "demo.pdf").run {
                FileOutputStream(this).use { output ->
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
                filePath.value = path
            }
        }
}
