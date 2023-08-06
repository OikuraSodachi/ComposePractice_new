package com.todokanai.composepractice.compose.presets.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectDialog(
    modifier : Modifier,
    title:String ?= null,
    items: List<Pair<String,()->Unit>>,
    selectedItems:List<String>,
    onDismissRequest:()->Unit,
    confirmText:String ?=null,
    dismissText:String ?=null,
    onConfirm:(()->Unit)? =null,
    onCancel:(()->Unit)? =null
){
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            title?.let{
                Text(title)
            }
                },
        text = {
            LazyColumn() {
                items(items.size) {
                    val item = items[it]
                    Row(
                        modifier = Modifier
                            .padding(0.dp,2.dp)
                            .clickable{item.second()},
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = selectedItems.contains(item.first),
                            onClick = { }
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Text(text = item.first)

                    }
                }
            }
               },
        confirmButton = {
            onConfirm?.let{
                Button(
                    onClick = { onConfirm() }
                ) {
                    confirmText?.let{
                        Text(confirmText)
                    }
                }
            }
        },
        dismissButton = {
            onCancel?.let{
                Button(
                    onClick = { onCancel() }
                ) {
                    dismissText?.let{
                        Text(dismissText)
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun SelectDialogPreview(){
    SelectDialog(
        modifier = Modifier,
        title = null,
        items = listOf(Pair("o11111111111",{}),Pair("o2222222222",{})),
        selectedItems = listOf("o2222222222"),
        onDismissRequest = {},
        dismissText = "test",
        confirmText = null,
        onConfirm = {},
        onCancel = {}
    )
}