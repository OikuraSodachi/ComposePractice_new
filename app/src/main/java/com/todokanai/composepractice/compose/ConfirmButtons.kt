package com.todokanai.composepractice.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_UNZIP

@Composable
fun ConfirmButtons(
    modifier: Modifier,
    confirm:()->Unit,
    cancel:()->Unit,
    mode:Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        TextButton(
            onClick = { cancel() },
            Modifier.weight(1f)
        ) {
            Text("Cancel")
        }
        TextButton(
            onClick = { confirm() },
            Modifier.weight(1f)
        ) {
            when(mode){
                CONFIRM_MODE_MOVE ->{
                    Text("Move")
                }
                CONFIRM_MODE_COPY ->{
                    Text("Copy")
                }
                CONFIRM_MODE_UNZIP ->{
                    Text("Unzip")
                }
            }
        }
    }
}

@Preview
@Composable
private fun ConfirmButtonsPreview(){
    Surface() {
        ConfirmButtons(
            modifier = Modifier,
            confirm = {},
            cancel = {},
            mode = CONFIRM_MODE_UNZIP
        )
    }
}