package com.todokanai.composepractice.compose.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.todokanai.composepractice.compose.presets.dialog.EditTextDialog

@Composable
fun RenameDialog(
    onConfirm: (text:String) -> Unit,
    onCancel: () -> Unit
){
    EditTextDialog(
        modifier = Modifier,
        title = "Rename",
        defaultText = "Name",
        onConfirm = {onConfirm(it)},
        onCancel = {onCancel()}
    )
}

@Preview
@Composable
private fun RenameDialogPreview(){
    RenameDialog(
        onConfirm = {},
        onCancel = {}
    )
}