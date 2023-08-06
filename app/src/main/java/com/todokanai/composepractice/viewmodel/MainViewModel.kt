package com.todokanai.composepractice.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.todokanai.composepractice.myobjects.Constants
import com.todokanai.composepractice.variables.Variables
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(){

    private val currentPath = Variables.currentPath
    private val selectMode = Variables.selectMode
    private val vars = Variables()

    fun getPermission(activity: Activity){
        if (!checkPermission(activity)) {
            requestPermission(activity)
        }
        requestStorageManageAccess(activity)
        vars.updateCurrentPath(currentPath.value)
    }

    private fun requestPermission(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                activity,
                "Storage permission is requires,please allow from settings",
                Toast.LENGTH_SHORT
            ).show()
        } else ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            111
        )
    }

    private fun checkPermission(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStorageManageAccess(activity: Activity) {
        if (Environment.isExternalStorageManager()) {
            vars.updatePhysicalStorageList(getPhysicalStorages(activity))
        } else {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            val uri: Uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivity(intent)
        }
    }
    private fun getPhysicalStorages(context: Context):List<File>{
        val defaultStorage = Environment.getExternalStorageDirectory()
        val volumes = context.getSystemService(StorageManager::class.java)?.storageVolumes
        val storageList = mutableListOf<File>(defaultStorage)
        volumes?.forEach { volume ->
            if (!volume.isPrimary && volume.isRemovable) {
                val sdCard = volume.directory
                sdCard?.let{
                    storageList.add(it)
                }
            }
        }
        return storageList
    }

    fun onBackPressed(toStorageFrag:()->Unit){
        when(selectMode.value){
            Constants.MULTI_SELECT_MODE -> {
                vars.updateSelectMode(Constants.DEFAULT_MODE)
            } else -> {
            if(File(currentPath.value.parent).listFiles()==null){
                toStorageFrag()
            }else {
                vars.updateCurrentPath(currentPath.value.parentFile)
            }
            }
        }
    }
}