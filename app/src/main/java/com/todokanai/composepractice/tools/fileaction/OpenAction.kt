package com.todokanai.composepractice.tools.fileaction

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import java.io.File
import java.util.Locale

class OpenAction {

    fun openFile(context: Context, selectedFile: File){
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            intent.setDataAndType(
                FileProvider.getUriForFile(context,
                    "${context.packageName}.provider",
                    selectedFile
                ), getMimeType_td(selectedFile.name)
            )
            ActivityCompat.startActivity(context, intent, null)
        } catch (e: Exception) {
            Toast.makeText(
                context.applicationContext,
                "Cannot open the file",
                Toast.LENGTH_SHORT
            ).show()
            println(e)
        }
    }   //  Done

    /**
     * Function to get the MimeType from a filename by comparing it's file extension
     * @author Neeyat Lotlikar
     * @param filename String name of the file. Can also be a path.
     * @return String MimeType */
    fun getMimeType_td(filename: String): String = if (filename.lastIndexOf('.') == -1)
        "resource/folder"
    else
        when (filename.subSequence(
            filename.lastIndexOf('.'),
            filename.length
        ).toString().lowercase(Locale.ROOT)) {
            ".doc", ".docx" -> "application/msword"
            ".pdf" -> "application/pdf"
            ".ppt", ".pptx" -> "application/vnd.ms-powerpoint"
            ".xls", ".xlsx" -> "application/vnd.ms-excel"
            ".zip", ".rar" -> "application/x-wav"
            ".7z" -> "application/x-7z-compressed"
            ".rtf" -> "application/rtf"
            ".wav", ".mp3", ".m4a", ".ogg", ".oga", ".weba" -> "audio/*"
            ".ogx" -> "application/ogg"
            ".gif" -> "image/gif"
            ".jpg", ".jpeg", ".png", ".bmp" -> "image/*"
            ".csv" -> "text/csv"
            ".m3u8" -> "application/vnd.apple.mpegurl"
            ".txt", ".mht", ".mhtml", ".html" -> "text/plain"
            ".3gp", ".mpg", ".mpeg", ".mpe", ".mp4", ".avi", ".ogv", ".webm" -> "video/*"
            else -> "*/*"
        }
}