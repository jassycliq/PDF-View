package com.jassycliq.pdfview

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Bitmap.Config.RGB_565
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color.WHITE
import android.graphics.pdf.PdfRenderer
import android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
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
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.SoftReference

internal class PdfViewModel(
    private val pdf: File?,
    private val isLowRam: Boolean = false,
) : ViewModel() {
    private lateinit var finalPdf: Bitmap
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            createPDF()
        }
    }

    override fun onCleared() {
        finalPdf.recycle()
        super.onCleared()
    }

    private fun createPDF() = pdf?.let {
        finalPdf = when (SDK_INT < O || isLowRam) {
            true -> pdf.createImageList()
                .renderCombinedPDFLowMem()
                .decodeSampledBitmapFromFile()
            false -> pdf.createImageList()
                .renderCombinedPDF()
        }.apply { prepareToDraw() }

        _uiState.update { currentState ->
            currentState.copy(pdf = finalPdf)
        }
    }

    private fun File.createImageList(): Set<SoftReference<Bitmap>> {
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

    private fun Set<SoftReference<Bitmap>>.renderCombinedPDF(): Bitmap {
        val width = maxOf { it.get()?.width ?: 0 }
        val height = sumOf { it.get()?.height ?: 0 }
        val bitmap = Bitmap.createBitmap(width, height, RGB_565)
        val canvas = Canvas(bitmap).apply { drawColor(WHITE) }
        var previousHeight = 0f

        forEach { pdfPage ->
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
        return bitmap
    }

    private fun Set<SoftReference<Bitmap>>.renderCombinedPDFLowMem(): File =
        File.createTempFile("final", ".jpg").apply {
            delete()
            with(renderCombinedPDF()) {
                compress(JPEG, 100, FileOutputStream(this@apply))
                recycle()
            }
        }

    private fun File.decodeSampledBitmapFromFile(): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(this@decodeSampledBitmapFromFile.path, this)
            // Calculate inSampleSize
            // TODO: Should probably look into doing some math with screen size
            inSampleSize = 2
            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(this@decodeSampledBitmapFromFile.path, this)
        }
    }
}

internal class PdfViewModelFactory(private val pdfFile: File?, private val isLowRam: Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PdfViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PdfViewModel(pdfFile, isLowRam) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
