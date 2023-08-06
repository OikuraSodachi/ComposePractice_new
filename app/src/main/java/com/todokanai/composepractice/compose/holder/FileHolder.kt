package com.todokanai.composepractice.compose.holder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toUri
import com.todokanai.composepractice.compose.presets.image.MyAsyncImage
import com.todokanai.composepractice.tools.Icons
import com.todokanai.composepractice.tools.independent.readableFileSize_td
import java.io.File
import java.text.DateFormat

@Composable
fun FileHolder(
    modifier: Modifier,
    file: File
) {
    val context = LocalContext.current
    val icons = Icons(context)
    val name = file.name
    val lastModified = DateFormat.getDateTimeInstance().format(file.lastModified())
    val extension = file.extension
    val size =
        if(file.isDirectory) {
            val subFiles = file.listFiles()
            if(subFiles == null){
                "null"
            }else {
                "${subFiles.size} 개"
            }
        } else {
            readableFileSize_td(file.length())
        }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        val (fileImage) = createRefs()
        val (fileName) = createRefs()
        val (fileDate) = createRefs()
        val (fileSize) = createRefs()

        if(extension == "jpg") {
            MyAsyncImage(
                modifier = Modifier
                    .constrainAs(fileImage) {
                        start.linkTo(parent.start)
                    }
                    .width(50.dp)
                    .padding(5.dp),
                data = file.toUri()
            )
        } else {
            Image(
                icons.thumbnail(file).asImageBitmap(),
                null,
                modifier = Modifier
                    .constrainAs(fileImage) {
                        start.linkTo(parent.start)
                    }
                    .width(50.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
            )
        }



        Text(
            size,
            fontSize = 15.sp,
            maxLines = 1,
            modifier = Modifier
                .constrainAs(fileSize) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .wrapContentWidth()
                .padding(4.dp)
        )

        Text(
            name,
            fontSize = 18.sp,
            maxLines = 1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(fileName) {
                    start.linkTo(fileImage.end)
                    end.linkTo(fileSize.start)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                }
                .height(30.dp)
                .padding(4.dp)
        )

        Text(
            lastModified,
            fontSize = 15.sp,
            maxLines = 1,
            modifier = Modifier
                .constrainAs(fileDate) {
                    start.linkTo(fileImage.end)
                    end.linkTo(fileSize.start)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(fileName.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(4.dp)
        )
    }
}