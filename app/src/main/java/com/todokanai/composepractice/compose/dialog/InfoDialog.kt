package com.todokanai.composepractice.compose.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.todokanai.composepractice.tools.independent.getTotalSize_td
import com.todokanai.composepractice.tools.independent.readableFileSize_td
import java.io.File


@Composable
fun InfoDialog(
    files:List<File>,
    onCancel:()->Unit
){
    val list = files.toTypedArray()
    val selectedNumber = "${list.size} 개"
    val sizeText : String = readableFileSize_td( getTotalSize_td(list))

    AlertDialog(
        onDismissRequest = {onCancel()},
        title = {Text("")},
        text= {
              InfoChart(
                  modifier = Modifier,
                  selectedNumber = selectedNumber,
                  sizeText = sizeText
              )
        },
        dismissButton = {          Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {onCancel()}
        ){
            Text(text = "Dismiss")
        }},
        confirmButton = {}
    )
}

@Composable
private fun InfoColumn(
    modifier: Modifier,
    title:String,
    info:String
){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ){
        Text(title)
        Text(info)
    }
}

@Composable
private fun InfoChart(
    modifier: Modifier,
    selectedNumber:String,
    sizeText:String
){
    Column(
        modifier = modifier
    ) {
        InfoColumn(
            modifier = Modifier,
            title = "Selected",
            info = selectedNumber
        )
        InfoColumn(
            modifier = Modifier,
            title = "Size",
            info = sizeText
        )
    }
}

@Preview
@Composable
private fun InfoDialogPreview(){
    Surface() {
        InfoDialog(
            files = emptyList()
        ) {}
    }
}