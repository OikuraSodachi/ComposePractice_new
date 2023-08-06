package com.todokanai.composepractice.tools.fileaction

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/** ViewModel 이상 단계에서 보이면 안됨 */
class NewFolderAction {
    fun newFolderAction(path: File, folderName:String,onComplete:()->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            File(path.absolutePath, folderName).mkdir()
        }.invokeOnCompletion {
            onComplete()
        }
    }       // Done
}