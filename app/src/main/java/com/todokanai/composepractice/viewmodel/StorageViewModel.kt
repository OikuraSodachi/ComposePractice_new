package com.todokanai.composepractice.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.todokanai.composepractice.tools.independent.exit_td
import com.todokanai.composepractice.variables.Variables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor() : ViewModel(){

    private val vars = Variables()
    fun updateCurrentPath(file:File) = vars.updateCurrentPath(file)

    val storageList : StateFlow<List<File>>
        get() = Variables.physicalStorageList

    fun button1(){

    }

    fun exit(activity:Activity) = exit_td(activity)
}