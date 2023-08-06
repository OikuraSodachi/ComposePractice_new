package com.todokanai.composepractice.tools.fileaction

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
/** ViewModel 이상 단계에서 보이면 안됨 */
class UnzipAction {

    fun unzipHere_Wrapper(
        zipFile:ZipFile,
        target: File,
        progressCallback : (progress:Int)->Unit,
        onComplete : ()->Unit,
        onSpaceRequired : ()->Unit
    ){

        if(zipFile.entries().toList().sumOf{it.size}>=target.freeSpace){
            onSpaceRequired()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                unzipHere_td(zipFile, target, progressCallback)
            }.invokeOnCompletion {
                onComplete()
            }
        }
    }

    fun unzip_Wrapper(
        zipFile:ZipFile,
        target: File,
        progressCallback : (progress:Int)->Unit,
        onComplete : ()->Unit,
        onSpaceRequired : ()->Unit
    ){
        if(zipFile.entries().toList().sumOf{it.size}>=target.freeSpace){
            onSpaceRequired()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                unzip_td(zipFile, target, progressCallback)
            }.invokeOnCompletion {
                onComplete()
            }
        }
    }

    private fun unzipHere_td(zipFile: ZipFile, target: File,callback:(progress:Int)->Unit) {
        val buffer = ByteArray(1024)
        val totalBytes = zipFile.entries().toList().sumOf{it.size}
        var processedBytes = 0L

        var prevProgress = 0
        val entries = zipFile.entries()

        while (entries.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            val fileName = entry.name
           // val destFilePath = target.resolve(fileName).toPath()
            val destFilePath = target.resolve(fileName)

            if (entry.isDirectory) {
                Files.createDirectories(destFilePath.toPath())
            } else {
                val parentDir = destFilePath.parentFile
                if (!parentDir.exists()) {
                    parentDir.mkdirs()
                }

                zipFile.getInputStream(entry).use { inputStream ->
                    FileOutputStream(destFilePath).use { outputStream ->
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                            processedBytes += bytesRead.toLong()
                            // Calculate and print progress
                            val progress = (100*processedBytes / totalBytes).toInt()
                            if(prevProgress!=progress || processedBytes == totalBytes) {
                                callback(progress)
                                prevProgress = progress
                            }
                        }
                    }
                }
            }
        }
    }

    private fun unzip_td(zipFile: ZipFile,target:File,callback: (progress: Int) -> Unit) {
        val folder = File("${target.toPath()}/${File(zipFile.name).nameWithoutExtension}")
        if(!folder.exists()){
            folder.mkdir()
        }
        unzipHere_td(zipFile,folder,callback)
    }
}