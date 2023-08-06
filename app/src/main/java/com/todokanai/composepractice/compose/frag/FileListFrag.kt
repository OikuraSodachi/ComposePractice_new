package com.todokanai.composepractice.compose.frag

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.todokanai.composepractice.compose.BottomButtons
import com.todokanai.composepractice.compose.ConfirmButtons
import com.todokanai.composepractice.compose.dialog.InfoDialog
import com.todokanai.composepractice.compose.dialog.RenameDialog
import com.todokanai.composepractice.compose.dialog.ZipDialog
import com.todokanai.composepractice.compose.holder.FileHolder
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_UNZIP
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_UNZIP_HERE
import com.todokanai.composepractice.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.composepractice.viewmodel.FileListViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileListFrag(
    modifier: Modifier,
    viewModel: FileListViewModel
) {
    val fileList = viewModel.sortedFileList.collectAsStateWithLifecycle()
    val selectMode = viewModel.selectMode.collectAsStateWithLifecycle()
    val currentPath = viewModel.currentPath.collectAsStateWithLifecycle()
    val selectedList = viewModel.selectedList.collectAsStateWithLifecycle()     // State<List<File>>
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(fileList.value.size) {
                val file = fileList.value[it]
                val isSelected = selectedList.value.contains(file)      // val file:File
                FileHolder(
                    modifier = Modifier
                        .background(if (isSelected) Color.LightGray else Color.Transparent)
                        .combinedClickable(
                            onClick = {
                                viewModel.onItemClick(
                                    context,
                                    file
                                )
                            },
                            onLongClick = {
                                viewModel.onItemLongClick(file)
                            }
                        ),
                    file = file
                )
            }
        }
        var zipDialog by remember {mutableStateOf(false)}
        if(zipDialog){
            ZipDialog(
                onConfirm = { viewModel.zip(it) },
                onCancel =  { zipDialog = false }
            )
        }
        var renameDialog by remember {mutableStateOf(false)}
        if(renameDialog){
            RenameDialog(
                onConfirm = { viewModel.rename(it) },
                onCancel = {renameDialog = false}
            )
        }
        var infoDialog by remember {mutableStateOf(false)}
        if(infoDialog){

            InfoDialog(
                files = selectedList.value,
                onCancel =  { infoDialog = false }
            )
        }
        when (selectMode.value) {
            MULTI_SELECT_MODE -> {
                BottomButtons(
                    modifier = Modifier,
                    move = { viewModel.moveMode() },
                    copy = { viewModel.copyMode() },
                    delete = { viewModel.delete() },
                    zip = { zipDialog = true },
                    unzip = { viewModel.unzipMode() },
                    unzipHere = {viewModel.unzipHereMode()},
                    rename = { renameDialog = true },
                    info = {infoDialog = true},
                    selectedList = selectedList.value
                )
            }
            CONFIRM_MODE_MOVE -> {
                ConfirmButtons(
                    modifier = Modifier,
                    confirm = { viewModel.confirm(currentPath.value) },
                    cancel = { viewModel.cancel() },
                    mode = CONFIRM_MODE_MOVE
                )
            }
            CONFIRM_MODE_COPY -> {
                ConfirmButtons(
                    modifier = Modifier,
                    confirm = { viewModel.confirm(currentPath.value) },
                    cancel = { viewModel.cancel() },
                    mode = CONFIRM_MODE_COPY
                )
            }
            CONFIRM_MODE_UNZIP, CONFIRM_MODE_UNZIP_HERE -> {
                ConfirmButtons(
                    modifier = Modifier,
                    confirm = { viewModel.confirm(currentPath.value) },
                    cancel = { viewModel.cancel() },
                    mode = CONFIRM_MODE_UNZIP
                )
            }
        }
    }
    println("Recomposition: FileListFrag")
}

@Preview
@Composable
private fun FileListFragPreview(){
    Surface {
        FileListFrag(Modifier, FileListViewModel())
    }
}