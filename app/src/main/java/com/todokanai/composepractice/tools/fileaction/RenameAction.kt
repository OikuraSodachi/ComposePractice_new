package com.todokanai.composepractice.tools.fileaction

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class RenameAction {

    fun renameAction(
        selectedFile: File,
        name:String,
        onComplete:()->Unit
    ){
        CoroutineScope(Dispatchers.IO).launch {
            selectedFile.renameTo(File("${selectedFile.parent}/$name"))
        }.invokeOnCompletion {
            onComplete()
        }
    }       // Done
}