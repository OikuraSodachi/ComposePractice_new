package com.todokanai.composepractice.compose.holder

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.name

@Composable
fun DirectoryHolder(
    modifier: Modifier,
    pathName: File
){
    Row(
        modifier = modifier
            .wrapContentWidth()
            .fillMaxHeight()
    ){
        Text(text = "/${pathName.name}",
            Modifier
                .padding(horizontal = 4.dp)
                .align(Alignment.CenterVertically),
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
private fun DirectoryHolderPreview(){
    Surface() {
        DirectoryHolder(
            modifier = Modifier,
            pathName = File("TestPath")
        )
    }
}