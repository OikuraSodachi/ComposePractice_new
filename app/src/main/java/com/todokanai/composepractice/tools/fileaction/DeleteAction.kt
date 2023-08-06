package com.todokanai.composepractice.tools.fileaction

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
/** ViewModel 이상 단계에서 보이면 안됨 */
class DeleteAction {
    fun deleteFiles(
        files:Array<File>,
        progressCallback:(progress:Int)->Unit?,
        onComplete:()->Unit?
    ){
        /*
        fun getFileNumber(files:Array<File>):Int{
            var total = 0
            for (file in files) {
                if (file.isDirectory) {
                    total += getFileNumber(file.listFiles() ?: emptyArray())
                }
                total ++
            }
            return total
        }
        val totalNumber = getFileNumber(files)

         */
        CoroutineScope(Dispatchers.IO).launch {
            var deleteProgress = 0
            fun deleteRecursivePart(
                file:File,
                callback:() -> Unit
            ){
                if(file.isDirectory){
                    file.listFiles()?.forEach {
                        deleteRecursivePart(it,callback)
                    }
                }
                file.delete()
                deleteProgress++
                callback()
            }
            files.forEach{ file ->
                deleteRecursivePart(
                    file,
                    callback = { progressCallback(deleteProgress)})
            }
        }.invokeOnCompletion {
            onComplete()
        }
    }
}