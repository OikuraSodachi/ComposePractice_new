package com.todokanai.composepractice.tools

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.todokanai.composepractice.R
import java.io.File

class Icons(val context: Context) {

    private val thumbnailFolder =
        ContextCompat.getDrawable(context, R.drawable.ic_baseline_folder_24)?.toBitmap()!!
    private val thumbnailPdf = ContextCompat.getDrawable(context, R.drawable.ic_pdf)?.toBitmap()!!
    private val thumbnailDefaultFile =
        ContextCompat.getDrawable(context, R.drawable.ic_baseline_insert_drive_file_24)
            ?.toBitmap()!!

    fun thumbnail(file: File): Bitmap {
        return if (file.isDirectory) {
            thumbnailFolder
        } else {
            when (file.extension) {
                "pdf" -> { thumbnailPdf }
                else -> { thumbnailDefaultFile }
            }
        }
    }
}