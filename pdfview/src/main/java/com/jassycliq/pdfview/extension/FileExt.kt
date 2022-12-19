package com.jassycliq.pdfview.extension

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.ParcelFileDescriptor
import android.os.ParcelFileDescriptor.MODE_READ_ONLY
import java.io.File

internal fun File.createPdfBitmap(isLowRamDevice: Boolean = false): Bitmap =
    when (SDK_INT < O || isLowRamDevice) {
        true ->
            createImageList()
                .combineBitmapsVertically()
                .compressToFile()
                .decodeSampledBitmapFromFile()
        false ->
            createImageList()
                .combineBitmapsVertically()
    }

/**
 * Creates a list of images from a PDF file
 */
internal fun File.createImageList(): List<Bitmap> {
    val input = ParcelFileDescriptor.open(this, MODE_READ_ONLY)

    return mutableListOf<Bitmap>().apply {
        PdfRenderer(input).use { pdf ->
            for (i in 0 until pdf.pageCount) {
                pdf.openPage(i).use { page ->
                    val bitmap = Bitmap.createBitmap(page.width * 2, page.height * 2, ARGB_8888)
                    page.render(bitmap, null, null, RENDER_MODE_FOR_DISPLAY)
                    add(bitmap)
                }
            }
        }
    }
}

internal fun File.decodeSampledBitmapFromFile(): Bitmap =
    // First decode with inJustDecodeBounds=true to check dimensions
    BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeFile(this@decodeSampledBitmapFromFile.path, this)
        // Calculate inSampleSize
        // TODO: Should probably look into doing some math with screen size
        inSampleSize = 2
        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false
        BitmapFactory.decodeFile(this@decodeSampledBitmapFromFile.path, this)
    }
