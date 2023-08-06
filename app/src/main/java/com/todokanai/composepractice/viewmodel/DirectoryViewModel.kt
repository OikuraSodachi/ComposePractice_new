package com.todokanai.composepractice.viewmodel

import androidx.lifecycle.ViewModel
import com.todokanai.composepractice.variables.Variables
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.nio.file.Path
import javax.inject.Inject

@HiltViewModel
class DirectoryViewModel @Inject constructor():ViewModel() {

    val dirTree = Variables.dirTree

    fun onDirectoryClick(path: File) = Variables().updateCurrentPath(path)
}