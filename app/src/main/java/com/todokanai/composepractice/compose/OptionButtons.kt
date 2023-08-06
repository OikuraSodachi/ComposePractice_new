package com.todokanai.composepractice.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.todokanai.composepractice.compose.dialog.SortDialog
import com.todokanai.composepractice.compose.presets.dialog.EditTextDialog
import com.todokanai.composepractice.compose.presets.dropdownmenu.MyDropdownMenu

@Composable
fun OptionButtons(
    modifier:Modifier,
    items:List<Pair<String,()->Unit>>,
    selectedItems:List<String>,
    storageList:List<Pair<String,()->Unit>>,
    exit:()->Unit,
    onConfirm:(String)->Unit
) {
    var showEditTextDialog by remember { mutableStateOf(false)}
    if(showEditTextDialog){
        EditTextDialog(
            modifier = Modifier,
            title = "Name of the new folder",
            defaultText = "folder name",
            onConfirm = {
                showEditTextDialog = false
                onConfirm(it)
                        },
            ) { showEditTextDialog = false }
    }

    var showSortDialog by remember { mutableStateOf(false)}
    if(showSortDialog){
        SortDialog(
            items = items,
            selectedItems =selectedItems,
            onCancel = {
                showSortDialog = false
            }
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val storageButtonExpanded = remember{ mutableStateOf(false)}
        TextButton(
            onClick = {storageButtonExpanded.value=!storageButtonExpanded.value},
            modifier = Modifier
                .weight(1f)
        ){
            Text("Storage")
            MyDropdownMenu(
                contents = storageList,
                expanded = storageButtonExpanded
            )
        }

        val moreButtonExpanded = remember{ mutableStateOf(false)}
        TextButton(
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(),
            onClick = { moreButtonExpanded.value = !moreButtonExpanded.value}
        ) {
            Text("More")

            MyDropdownMenu(
                contents = listOf(
                    Pair("Create New Folder",{ showEditTextDialog = true}),
                    Pair("Sort",{showSortDialog = true}),
                    Pair("Exit",{exit()})
                ),
                expanded = moreButtonExpanded
            )
        }
    }
}

@Preview
@Composable
private fun OptionButtonsPreview(){
    Surface() {
        OptionButtons(
            modifier = Modifier,
            exit = {},
            items = listOf(Pair("1",{}),Pair("2",{})),
            selectedItems = listOf("2"),
            storageList = listOf(Pair("Content1",{}),Pair("Content2",{})),
            onConfirm = {}
        )
    }
}