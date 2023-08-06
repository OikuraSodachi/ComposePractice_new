package com.todokanai.composepractice.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StorageMenuButtons(
    modifier : Modifier,
    button1:()-> Unit,
    exit:()-> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = {button1()}
        ){
            Text("Button1")
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = {exit()}
        ){
            Text("Exit")
        }
    }
}

@Preview
@Composable
private fun StorageMenuButtonsPreview(){
    StorageMenuButtons(
        modifier = Modifier,
        button1 = {},
        exit = {}
    )
}