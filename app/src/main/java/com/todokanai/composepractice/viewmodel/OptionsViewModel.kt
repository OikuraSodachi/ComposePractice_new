package com.todokanai.composepractice.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.todokanai.composepractice.tools.FileAction
import com.todokanai.composepractice.tools.independent.exit_td
import com.todokanai.composepractice.variables.Variables
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject constructor(): ViewModel(){

    private val currentPath = Variables.currentPath
    private val fAction by lazy{ FileAction() }
    private val vars = Variables()
    val storageList = Variables.physicalStorageList
    val sortMode = Variables.sortMode

    fun toPair(storageList: List<File>) = listToPair(storageList)

    fun newFolder(name:String) = fAction.newFolderAction(currentPath.value, name)

    fun exit(activity: Activity) = exit_td(activity)

    fun sortModeCallbackList() = vars.sortModeCallbackList()
    //------------------
    // private

    private fun listToPair(storageList:List<File>):List<Pair<String,()->Unit>>{
        val result = mutableListOf<Pair<String,()->Unit>>()
        storageList.forEach {
            result.add(Pair(it.absolutePath,{vars.updateCurrentPath(it)}))
        }
        return result
    }
}