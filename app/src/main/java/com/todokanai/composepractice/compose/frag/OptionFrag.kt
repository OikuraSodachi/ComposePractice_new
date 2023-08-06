package com.todokanai.composepractice.compose.frag

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.todokanai.composepractice.compose.OptionButtons
import com.todokanai.composepractice.compose.activity.MainActivity
import com.todokanai.composepractice.viewmodel.OptionsViewModel

@Composable
fun OptionFrag(
    modifier: Modifier,
    activity: Activity,
    viewModel: OptionsViewModel
) {
    val sortModeSelected = viewModel.sortMode.collectAsStateWithLifecycle()
    val storageList = viewModel.storageList.collectAsStateWithLifecycle()

     Column(
         modifier = modifier
     ) {
         OptionButtons(
             modifier = Modifier,
             exit = {viewModel.exit(activity)},
             items = viewModel.sortModeCallbackList(),
             selectedItems = listOf(sortModeSelected.value),
             storageList = viewModel.toPair(storageList.value),
             onConfirm = {viewModel.newFolder(it)}
         )
     }
}

@Preview
@Composable
private fun OptionFragPreview(){
    Surface() {
        OptionFrag(
            modifier = Modifier,
            activity = MainActivity(),
            viewModel = OptionsViewModel()
        )
    }
}