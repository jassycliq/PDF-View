package com.jassycliq.pdfviewexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jassycliq.pdfview.PdfView
import com.jassycliq.pdfview.rememberPdfViewState
import com.jassycliq.pdfviewexample.ui.theme.PDFViewTheme
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    private var file by mutableStateOf<File?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Should do this in VM but too lazy
        val inputFile = applicationContext.resources.openRawResource(R.raw.demo)
        inputFile.use { input ->
            file = File(cacheDir, "demo.pdf")
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }

        setContent {
            PDFViewTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    PdfView(state = rememberPdfViewState(file))
                }
            }
        }
    }
}
