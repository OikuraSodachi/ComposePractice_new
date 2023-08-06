package com.todokanai.composepractice.tools.fileaction

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/** ViewModel 이상 단계에서 보이면 안됨 */
class CopyAction {

    fun copyFiles(
        files:Array<File>,
        target: File,
        progressCallback:(progress:Int)->Unit,
        onComplete:()->Unit?,
        onSpaceRequired:()->Unit?
    ){
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
        if(getTotalSize(files)>=target.freeSpace){
            onSpaceRequired()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val totalSize = getTotalSize(files)
                var bytesProgress = 0L

                suspend fun copyFileRecursivePart(
                    file: File,
                    target: File,
                    callback: (percent: Int) -> Unit
                ) {
                    var prevProgress = 0
                    withContext(Dispatchers.IO) {
                        if (file.isDirectory) {
                            if (!target.exists()) {
                                target.mkdir()
                            }
                            file.listFiles()?.forEach {
                                val newFile = target.toPath().resolve(it.name)
                                copyFileRecursivePart(it, File("$newFile"), callback)
                            }
                        } else {
                            val inputStream = FileInputStream(file)
                            val outputStream = FileOutputStream(target)
                            val buffer = ByteArray(1024)
                            var bytesRead: Int

                            while (inputStream.read(buffer).also { bytesRead = it } > 0) {
                                outputStream.write(buffer, 0, bytesRead)
                                bytesProgress += bytesRead
                                /** */
                                val progress = (100 * bytesProgress / totalSize).toInt()
                                if (prevProgress != progress || bytesProgress == totalSize) {      // 진행도 변경되거나 전체 파일의 끝일때
                                    callback(progress)
                                    prevProgress = progress
                                }
                            }
                            inputStream.close()
                            outputStream.close()
                        }
                    }
                }
                files.forEach { file ->
                    val tempTarget = File("${target.toPath()}/${file.name}")
                    copyFileRecursivePart(file, tempTarget) { progressCallback(it) }
                }
            }.invokeOnCompletion {
                onComplete()
            }
        }
    }       // Done
}