package com.jassycliq.pdfview.extension

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.Config.RGB_565
import android.graphics.Canvas
import android.graphics.Color.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Stacks a list of bitmaps vertically into one bitmap.
 */
internal fun List<Bitmap>.combineBitmapsVertically(): Bitmap {
    val width = maxOf { it.width }
    val height = sumOf { it.height }
    val bitmap = Bitmap.createBitmap(width, height, RGB_565)
    val canvas = Canvas(bitmap).apply { drawColor(WHITE) }
    var previousHeight = 0f

    forEach { pdfPage ->
        try {
            canvas.apply {
                drawBitmap(pdfPage, 0f, previousHeight, null)
                previousHeight += pdfPage.height
                save()
            }
        } finally {
            pdfPage.recycle()
        }
    }
    return bitmap
}

/**
 * Compresses a bitmap into a file in JPEG format and returns the file. This is used to reduce the
 * memory usage of the bitmap.
 */
internal fun Bitmap.compressToFile(): File =
    File.createTempFile("final", ".jpg").apply {
        compress(JPEG, 100, FileOutputStream(this))
        recycle()
    }

// TODO: Begins work on potentially new method to tile render PDFs
internal fun Bitmap.convertToInputStream(): InputStream =
    File.createTempFile("final", ".jpg").apply {
        compress(JPEG, 100, FileOutputStream(this))
        recycle()
    }.inputStream()
