package com.todokanai.composepractice.compose.holder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.todokanai.composepractice.tools.independent.readableFileSize_td
import java.io.File

@Composable
fun StorageHolder(
    modifier : Modifier,
    storage : File
){
    val storageSize = storage.totalSpace
    val freeSize = storage.freeSpace
    val progress  = ((storageSize.toDouble()-freeSize.toDouble())/storageSize.toDouble()).toFloat()
    val used = readableFileSize_td(storageSize-freeSize)
    val total = readableFileSize_td(storageSize)
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .height(30.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = storage.absolutePath
                )

                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = "${used}/${total}",
                    textAlign = TextAlign.End
                )
            }
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                progress = progress,
                trackColor = Color.LightGray,
                color = Color.Red
            )
        }
    }
}

@Preview
@Composable
private fun StorageHolderPreview(){
    Surface() {
        StorageHolder(
            modifier = Modifier,
            storage = File("Storage")
        )
    }
}