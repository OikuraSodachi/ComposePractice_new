package com.todokanai.composepractice.tools.fileaction

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
/** ViewModel 이상 단계에서 보이면 안됨 */
class ZipAction {

    /** 여유공간 체크할때 단순 files의 크기 합을 기준으로 체크함 */
    fun zipFiles(
        files :Array<File>,
        zipFile : File,
        progressCallback : (progress:Int)->Unit,
        onComplete : ()->Unit,
        onSpaceRequired : ()->Unit
    ) {
        fun getTotalSize(files: Array<File>): Long {
            var totalSize: Long = 0
            for (file in files) {
                if (file.isFile) {
                    totalSize += file.length()
                } else if (file.isDirectory) {
                    totalSize += getTotalSize(
                        file.listFiles() ?: emptyArray()
                    )
                }
            }
            return totalSize
        }

        fun zip_td(file: File, zipFile: File, callback: (progress: Int) -> Unit) {
            fun getTotalBytesToCompress(directory: File): Long {
                var totalBytes = 0L
                directory.walkTopDown().forEach {
                    if (it.isFile) {
                        totalBytes += it.length()
                    }
                }
                return totalBytes
            }
            val totalBytesToCompress = getTotalBytesToCompress(file)
            var compressedBytes = 0L
            var prevProgress = 0

            // 파일을 zip으로 압축
            zipFile.parentFile.mkdirs()
            ZipOutputStream(zipFile.outputStream()).use { zos ->
                file.walkTopDown().forEach { sFile ->
                    if (sFile.isFile) {
                        val entryName = file.toPath().relativize(sFile.toPath()).toString().replace("\\", "/")
                        val zipEntry = ZipEntry(entryName)
                        zos.putNextEntry(zipEntry)

                        // 버퍼를 활용하여 파일 읽기 및 쓰기
                        val buffer = ByteArray(1024)
                        sFile.inputStream().use { input ->
                            var bytesRead = input.read(buffer)
                            while (bytesRead != -1) {
                                zos.write(buffer, 0, bytesRead)
                                compressedBytes += bytesRead.toLong()
                                val progress = (compressedBytes * 100 / totalBytesToCompress).toInt()
                                if(prevProgress!=progress || compressedBytes==totalBytesToCompress) {
                                    callback(progress)
                                    prevProgress = progress
                                }
                                bytesRead = input.read(buffer)
                            }
                        }
                        zos.closeEntry()
                    }
                }
            }
        }

        if(getTotalSize(files)>=zipFile.parentFile.freeSpace){
            onSpaceRequired()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                files.forEach { file ->
                    zip_td(file, zipFile, progressCallback)
                }
            }.invokeOnCompletion {
                onComplete()
            }
        }
    }
}