package com.jassycliq.pdfview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import java.io.File
import java.io.FileOutputStream

internal fun List<Bitmap>.combineBitmapsVertically(): Bitmap {
    val width = maxOf { it.width }
    val height = sumOf { it.height }
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap).apply { drawColor(Color.WHITE) }
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

internal fun List<Bitmap>.combineBitmapsVerticallyLowMem(): File =
    File.createTempFile("final", ".jpg").apply {
        delete()
        with(combineBitmapsVertically()) {
            compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(this@apply))
            recycle()
        }
    }
