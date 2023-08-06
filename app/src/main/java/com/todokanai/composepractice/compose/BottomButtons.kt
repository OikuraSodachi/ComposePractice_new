package com.todokanai.composepractice.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.todokanai.composepractice.compose.presets.dropdownmenu.MyDropdownMenu
import java.io.File

@Composable
fun BottomButtons(
    modifier: Modifier,
    move:()->Unit,
    copy:()->Unit,
    delete:()->Unit,
    zip:()->Unit,
    unzip:()->Unit,
    unzipHere:()->Unit,
    rename:()->Unit,
    info:()->Unit,
    selectedList:List<File>
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        TextButton(
            onClick = { move() },
            Modifier.weight(1f)
        ) {
            Text("Move")
        }
        TextButton(
            onClick = { copy() },
            Modifier.weight(1f)
        ) {
            Text("Copy")
        }
        TextButton(
            onClick = { delete() },
            Modifier.weight(1f)
        ) {
            Text("Delete")
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentSize()
        ) {
            val expanded = remember {mutableStateOf(false)}

            fun contents(selectedList: List<File>) : List<Pair<String,()->Unit>> {
                val result = mutableListOf(Pair("Zip") { zip() },Pair("info"){info()})
                if(selectedList.size ==1) {
                    val selected = selectedList.first()
                    result.add(Pair("Rename") { rename() })

                    if(selected.extension == "zip"){
                        result.add(Pair("unzip") { unzip() })
                        result.add(Pair("unzip Here") { unzipHere() })
                    }
                }
                return result
            }
            TextButton(
                modifier = Modifier,
                onClick = { expanded.value = !expanded.value}
            ) {
                Text("More")

                MyDropdownMenu(
                    contents = contents(selectedList),
                    expanded = expanded
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomButtonsPreview(){
    Surface() {
        BottomButtons(
            Modifier,
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            {},
            emptyList()
        )
    }
}