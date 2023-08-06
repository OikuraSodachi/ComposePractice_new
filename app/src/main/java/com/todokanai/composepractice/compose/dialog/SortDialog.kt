package com.todokanai.composepractice.compose.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.todokanai.composepractice.compose.presets.dialog.SelectDialog

@Composable
fun SortDialog(
    items : List<Pair<String,()->Unit>>,
    selectedItems : List<String>,
    onCancel:()->Unit
){
    SelectDialog(
        modifier = Modifier,
        title = null,
        items = items,
        selectedItems = selectedItems,
        onDismissRequest = {onCancel()},
        onConfirm = null,
        onCancel = {onCancel()}
    )
}

@Preview
@Composable
private fun SortDialogPreview(){
    SortDialog(
        items = listOf(Pair("1",{}),Pair("2",{})),
        selectedItems = listOf("2"),
        onCancel = {}
    )
}