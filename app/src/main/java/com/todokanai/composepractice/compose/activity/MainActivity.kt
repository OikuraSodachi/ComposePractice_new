package com.todokanai.composepractice.compose.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.todokanai.composepractice.compose.frag.DirectoryFrag
import com.todokanai.composepractice.compose.frag.FileListFrag
import com.todokanai.composepractice.compose.frag.OptionFrag
import com.todokanai.composepractice.compose.frag.StorageFrag
import com.todokanai.composepractice.ui.theme.ComposePracticeTheme
import com.todokanai.composepractice.viewmodel.DirectoryViewModel
import com.todokanai.composepractice.viewmodel.FileListViewModel
import com.todokanai.composepractice.viewmodel.MainViewModel
import com.todokanai.composepractice.viewmodel.OptionsViewModel
import com.todokanai.composepractice.viewmodel.StorageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var activityResult: ActivityResultLauncher<String>
    private val mViewModel: MainViewModel by viewModels()
    private val fViewModel: FileListViewModel by viewModels()
    private val dViewModel: DirectoryViewModel by viewModels()
    private val oViewModel : OptionsViewModel by viewModels()
    private val sViewModel : StorageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(
                activity = this,
                mViewModel = mViewModel,
                fViewModel = fViewModel,
                sViewModel = sViewModel,
                dViewModel = dViewModel,
                oViewModel = oViewModel
            )
        }

        mViewModel.getPermission(this)

        activityResult =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted)
                    finish()
            }
    }
}

@Composable
private fun HomeScreen(
    activity: MainActivity,
    mViewModel:MainViewModel,
    fViewModel:FileListViewModel,
    sViewModel:StorageViewModel,
    dViewModel:DirectoryViewModel,
    oViewModel:OptionsViewModel
){
    ComposePracticeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            var isStorageFrag by remember{mutableStateOf(true)}

            activity.onBackPressedDispatcher.addCallback() {
                mViewModel.onBackPressed({isStorageFrag = true})
            }
            if(isStorageFrag){
                StorageFrag(
                    modifier =Modifier,
                    activity = activity,
                    viewModel = sViewModel,
                    exitStorageFrag ={isStorageFrag = false}
                )

            }else {
                Column() {
                    OptionFrag(
                        modifier = Modifier,
                        activity = activity,
                        viewModel = oViewModel
                    )
                    DirectoryFrag(
                        modifier = Modifier,
                        viewModel = dViewModel
                    )
                    FileListFrag(
                        modifier = Modifier,
                        viewModel = fViewModel
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        MainActivity(),
        MainViewModel(),
        FileListViewModel(),
        StorageViewModel(),
        DirectoryViewModel(),
        OptionsViewModel()
    )
}