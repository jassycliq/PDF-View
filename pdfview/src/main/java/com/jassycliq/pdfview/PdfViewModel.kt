package com.jassycliq.pdfview

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color.WHITE
import android.graphics.pdf.PdfRenderer
import android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
import android.os.ParcelFileDescriptor
import android.os.ParcelFileDescriptor.MODE_READ_ONLY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.SoftReference

class PdfViewModel(
    private val pdf: File?,
) : ViewModel() {
    private lateinit var finalPdf: Bitmap
    private val _uiState = MutableStateFlow(PdfState())
    val uiState: StateFlow<PdfState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            createPDF()
        }
    }

    override fun onCleared() {
        finalPdf.recycle()
        super.onCleared()
    }

    private suspend fun createPDF() = withContext(Dispatchers.IO) {
        pdf?.let {
            finalPdf = pdf.createImageList()
                .renderCombinedPDF()
                .decodeSampledBitmapFromFile()
                .apply { prepareToDraw() }
            _uiState.update { currentState ->
                currentState.copy(pdf = finalPdf)
            }
        }
    }

    private fun File.createImageList(): MutableSet<SoftReference<Bitmap>> {
        val input = ParcelFileDescriptor.open(this, MODE_READ_ONLY)
        val imageList = mutableSetOf<SoftReference<Bitmap>>()

        PdfRenderer(input).use { pdf ->
            for (i in 0 until pdf.pageCount) {
                pdf.openPage(i).use { page ->
                    val bitmap = Bitmap.createBitmap(page.width * 2, page.height * 2, ARGB_8888)
                    page.render(bitmap, null, null, RENDER_MODE_FOR_DISPLAY)
                    imageList.add(SoftReference(bitmap))
                }
            }
        }

        return imageList
    }

    private fun MutableSet<SoftReference<Bitmap>>.renderCombinedPDF(): File {
        val width = this.first().get()?.width ?: 0
        val height = (this.first().get()?.height ?: 0) * this.size
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap).apply { drawColor(WHITE) }
        var previousHeight = 0f

        this.forEach { pdfPage ->
            canvas.apply {
                pdfPage.get()?.let {
                    drawBitmap(it, 0f, previousHeight, null)
                    save()
                }
            }
            previousHeight += pdfPage.get()?.height ?: 0
            pdfPage.get()?.recycle()
            pdfPage.clear()
        }

        val file = File.createTempFile("final", ".jpg")
        bitmap.apply {
            compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
            recycle()
        }
        return file
    }

    private fun File.decodeSampledBitmapFromFile(): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(this@decodeSampledBitmapFromFile.path, this)
            // Calculate inSampleSize
            inSampleSize = 2
            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(this@decodeSampledBitmapFromFile.path, this)
        }
    }
}

class PdfViewModelFactory(private val pdfFile: File?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PdfViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PdfViewModel(pdfFile) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
