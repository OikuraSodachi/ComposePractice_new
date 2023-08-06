package com.todokanai.composepractice.tools

/** ~Action -> ViewModel에서 호출하는 method
 *
 *  웬만하면 여기서는 CoroutineScope가 등장하지 않도록 작성
 */
import android.content.Context
import com.todokanai.composepractice.tools.fileaction.CopyAction
import com.todokanai.composepractice.tools.fileaction.DeleteAction
import com.todokanai.composepractice.tools.fileaction.MoveAction
import com.todokanai.composepractice.tools.fileaction.NewFolderAction
import com.todokanai.composepractice.tools.fileaction.OpenAction
import com.todokanai.composepractice.tools.fileaction.RenameAction
import com.todokanai.composepractice.tools.fileaction.UnzipAction
import com.todokanai.composepractice.tools.fileaction.ZipAction
import com.todokanai.composepractice.variables.Variables
import java.io.File
import java.util.zip.ZipFile

class FileAction() {

    private val myNoti = MyNotification()
    private val lTool = LogTool()
    private fun toast(message: String) = lTool.makeShortToast(message)

    /** CoroutineScope 감싸고 invokeOnCompletion? */
    fun openAction(context:Context, selected: File){
        if (selected.isDirectory) {
            onComplete(selected,null)   // Folder 열기
        } else {
            OpenAction().openFile(context, selected)   // 파일이면 열기
        }
    }       // Done

    fun renameAction(selectedFile:File,name:String){
        RenameAction().renameAction(
            selectedFile = selectedFile,
            name = name,
            onComplete = {onComplete(selectedFile.parentFile,null)}
        )
    }       // Done

    fun copyAction(files:Array<File>, currentPath: File){
        CopyAction().copyFiles(
            files = files,
            target = currentPath,
            progressCallback = {  myNoti.copyProgressNoti(it) },
            onComplete = { onComplete(currentPath,"copied ${files.size} files") },
            onSpaceRequired = { onSpaceRequired() }
        )
    }       // Done

    fun moveAction(files:Array<File>, currentPath: File){
        MoveAction().moveAction(
            files = files,
            path = currentPath,
            progressCallback = {myNoti.moveProgressNoti(it)},
            onComplete = { onComplete(currentPath,"Moved ${files.size} files")},
            onSpaceRequired = {onSpaceRequired()}
        )
    }           // Done

    fun deleteAction(files:Array<File>){
        fun getFileNumber(files:Array<File>):Int{
            var total = 0
            for (file in files) {
                if (file.isDirectory) {
                    total += getFileNumber(file.listFiles() ?: emptyArray())
                }
                total ++
            }
            return total
        }
        val totalNumber = getFileNumber(files)
        DeleteAction().deleteFiles(
            files = files,
            progressCallback = {myNoti.deleteProgressNoti(it,totalNumber)},
            onComplete = {onComplete(files.first().parentFile,"deleted ${files.size} files and its subdirectories")})
    }       // Done
    fun newFolderAction(currentPath:File, folderName:String){
        NewFolderAction().newFolderAction(
            path = currentPath,
            folderName = folderName,
            onComplete = {onComplete(currentPath,null)}
        )
    }       // Done

    fun zipAction(files:Array<File>, zipFileName:String){
        val targetPath = files.first().parentFile
        val zipFile = File("$targetPath/$zipFileName.zip")
        ZipAction().zipFiles(
            files = files,
            zipFile = zipFile,
            progressCallback = {  myNoti.zipProgressNoti(it) },
            onComplete = { onComplete(targetPath,"zipped ${files.size} files") },
            onSpaceRequired = { onSpaceRequired() }
        )
    }

    fun unzipAction(zipFile:File, currentPath:File, unzipHere:Boolean){
        val file = ZipFile(zipFile)
        if(unzipHere){
            UnzipAction().unzipHere_Wrapper(
                zipFile = file,
                target = currentPath,
                progressCallback = {myNoti.unzipProgressNoti(it)},
                onComplete = {onComplete(currentPath.parentFile,"unzipped")},
                onSpaceRequired = {onSpaceRequired()}
            )
        } else {
            UnzipAction().unzip_Wrapper(
                zipFile = file,
                target = currentPath,
                progressCallback = {myNoti.unzipProgressNoti(it)},
                onComplete = {onComplete(currentPath,"unzipped")},
                onSpaceRequired = {onSpaceRequired()}
            )
        }
    }      // Done

    //-----------------------
    // 여기부터 Private

    private fun onSpaceRequired() = toast("Not enough space")

    /** path에 null 입력시 update 미발생
     *
     * message에 null 입력시 알림은 미발생
     */

    private fun onComplete(
        path:File?,
        message:String?
    ){
        path?.let {
            Variables().updateCurrentPath(path)
        }
        message?.let {
            myNoti.completedNotification(
                title = "",
                message = it
            )
        }
    }
}