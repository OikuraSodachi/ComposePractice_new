package com.todokanai.composepractice.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_UNZIP
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_UNZIP_HERE
import com.todokanai.composepractice.myobjects.Constants.DEFAULT_MODE
import com.todokanai.composepractice.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.composepractice.tools.FileAction
import com.todokanai.composepractice.variables.Variables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileListViewModel @Inject constructor():ViewModel(){

    val selectedList = Variables.selectedList
    val selectMode = Variables.selectMode
    val currentPath = Variables.currentPath
    val sortedFileList : StateFlow<List<File>> = Variables.currentFileList      // 원본 StateFlow

    /**
     *  큰 영향은 없지만 아마 Memory Leak?
     */
    private lateinit var selectedItem : File
    private val vars = Variables()
    private val fAction = FileAction()

    fun onItemClick(context: Context, selected: File){
        selectedItem = selected
        when (selectMode.value) {
            DEFAULT_MODE -> {
                fAction.openAction(context, selected)
            }
            MULTI_SELECT_MODE -> {
                vars.toggleSelection(selected)
            }
            else -> {
                if (selected.isDirectory) {
                    fAction.openAction(context, selected)
                }
            }
        }
    }
    fun onItemLongClick(selected: File){
        if (selectMode.value == DEFAULT_MODE) {
            vars.updateSelectMode(MULTI_SELECT_MODE)
            vars.toggleSelection(selected)
        }
    }
    fun copyMode() = vars.updateSelectMode(CONFIRM_MODE_COPY)
    fun moveMode() = vars.updateSelectMode(CONFIRM_MODE_MOVE)
    fun rename(name:String){
        fAction.renameAction(selectedList.value.first(), name)
        vars.updateSelectMode(DEFAULT_MODE)
    }

    fun delete(){
        fAction.deleteAction(selectedList.value.toTypedArray())
        vars.updateSelectMode(DEFAULT_MODE)
    }

    fun zip(name:String){
        val list = selectedList.value.toTypedArray()
        fAction.zipAction(list, name)
        vars.updateSelectMode(DEFAULT_MODE)
    }

    /**
     * 넘겨줄 file이 압축해제 가능한지 체크할것
     * 웬만하면 "압축해제 가능한 단일 파일"이 선택된 상황에만 unzip 활성화하기
     */
    fun unzipMode() = vars.updateSelectMode(CONFIRM_MODE_UNZIP)

    fun unzipHereMode() = vars.updateSelectMode(CONFIRM_MODE_UNZIP_HERE)

    fun confirm(currentPath:File){
        val list = selectedList.value.toTypedArray()

        when (selectMode.value) {       // selectMode : StateFlow<Int>
            CONFIRM_MODE_COPY -> {
                fAction.copyAction(list,currentPath)
                vars.updateSelectMode(DEFAULT_MODE)
            }

            CONFIRM_MODE_MOVE -> {
                fAction.moveAction(list,currentPath)
                vars.updateSelectMode(DEFAULT_MODE)
            }
            CONFIRM_MODE_UNZIP -> {
                fAction.unzipAction(list.first(),currentPath, unzipHere = false)
                vars.updateSelectMode(DEFAULT_MODE)
            }
            CONFIRM_MODE_UNZIP_HERE ->{
                fAction.unzipAction(list.first(), currentPath,unzipHere = true)
                vars.updateSelectMode(DEFAULT_MODE)
            }
        }
    }

    fun cancel() = vars.updateSelectMode(DEFAULT_MODE)
    //--------------------


}