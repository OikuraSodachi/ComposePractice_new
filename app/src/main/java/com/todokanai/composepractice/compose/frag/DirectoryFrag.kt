package com.todokanai.composepractice.compose.frag

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.todokanai.composepractice.compose.holder.DirectoryHolder
import com.todokanai.composepractice.viewmodel.DirectoryViewModel

@Composable
fun DirectoryFrag(
    modifier:Modifier,
    viewModel: DirectoryViewModel
){
    val items = viewModel.dirTree.collectAsStateWithLifecycle()

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {
        items(items.value.size) {
            val item = items.value[it]
            DirectoryHolder(
                modifier = Modifier
                    .clickable { viewModel.onDirectoryClick(item) },
                pathName = item
            )
        }
    }
}

@Preview
@Composable
private fun DirectoryRecyclerPreview(){
    Surface() {
        DirectoryFrag(
            modifier = Modifier,
            viewModel = DirectoryViewModel()
        )
    }
}