package com.todokanai.composepractice.tools.independent

/** 이 method들은 독립적으로 사용 가능함 */

import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionMethod
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Path
import java.text.DecimalFormat
import java.util.Arrays
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.absolutePathString
import kotlin.math.log10
import kotlin.math.pow

//----------------------
    /** Todokanai
     *
     * Function to get the character sequence from after the last instance of File.separatorChar in a path
     * @author Neeyat Lotlikar
     * @param path String path representing the file
     * @return String filename which is the character sequence from after the last instance of File.separatorChar in a path
     * if the path contains the File.separatorChar. Else, the same path.*/
    fun getFilenameForPath_td(path: String): String =
        if (!path.contains(File.separatorChar)) path
        else path.subSequence(
            path.lastIndexOf(File.separatorChar) + 1, // Discard the File.separatorChar
            path.length // parameter is used exclusively. Substring produced till n - 1 characters are reached.
        ).toString()

/** Todokanai */
fun readableFileSize_td(size: Long): String {
        if (size <= 0) return "0"
        val units = arrayOf("B", "kB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(
            size / 1024.0.pow(digitGroups.toDouble())
        ) + " " + units[digitGroups]
    }

    /** Todokanai
     *
     * 안드로이드에서만 적용 가능?
     */
    fun getPhysicalStorage_td(file: File): String {
        val path = file.absolutePath
        val firstSlashIndex = path.indexOf('/')
        val secondSlashIndex = path.indexOf('/', startIndex = firstSlashIndex + 1)
        val thirdSlashIndex = path.indexOf('/', startIndex = secondSlashIndex + 1)
        return if (thirdSlashIndex > secondSlashIndex) {
            path.substring(secondSlashIndex + 1, thirdSlashIndex)
        } else {
            path.substring(secondSlashIndex + 1)
        }
    }

    /** Todokanai
     *
     * 접근 권한이 없는 파일의 크기는 인식 못함 */
    fun getTotalSize_td(files: Array<File>): Long {
        var totalSize: Long = 0
        for (file in files) {
            if (file.isFile) {
                totalSize += file.length()
            } else if (file.isDirectory) {
                totalSize += getTotalSize_td(file.listFiles() ?: emptyArray())
            }
        }
        return totalSize
    }

/** Todokanai
 *
 * Directory의 갯수는 포함이 되지 않음 */
fun getFileNumber_td(files:Array<File>):Int{
    var total = 0
    for (file in files) {
        if (file.isFile) {
            total ++
        } else if (file.isDirectory) {
            total += getFileNumber_td(file.listFiles() ?: emptyArray())
        }
    }
    return total
}
/** Todokanai
 *
 * Directory와 File의 총 갯수*/
fun getFileAndFoldersNumber_td(files:Array<File>):Int{
    var total = 0
    for (file in files) {
        if (file.isFile) {
            total ++
        } else if (file.isDirectory) {
            total ++
            total += getFileNumber_td(file.listFiles() ?: emptyArray())
        }
    }
    return total
}


/** Todokanai */
fun dirTree_td(currentPath:File): List<File> {
        val result = mutableListOf<File>()
        var now = currentPath
        while (now.parent != null) {
            result.add(now)
            now = now.parentFile
        }
        return result.reversed()
    }

/** Todokanai */
fun compressFilesRecursivelyToZip_td(files: Array<File>, zipFile: File) {
    val buffer = ByteArray(1024)
    val zipOutputStream = ZipOutputStream(zipFile.outputStream())

    fun addToZip(file: File, parentPath: String = "") {
        val entryName = if (parentPath.isNotEmpty()) "$parentPath/${file.name}" else file.name

        if (file.isFile) {
            val zipEntry = ZipEntry(entryName)
            zipOutputStream.putNextEntry(zipEntry)

            val inputStream = FileInputStream(file)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } > 0) {
                zipOutputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            zipOutputStream.closeEntry()
        } else if (file.isDirectory) {
            val files = file.listFiles()
            files?.forEach { childFile ->
                addToZip(childFile, entryName)
            }
        }
    }
    for (file in files) {
        addToZip(file)
    }
    zipOutputStream.close()
    println("파일 압축이 완료되었습니다.")
}

/** Todokanai */
fun compressFilesRecursivelyWithZip4j_td(files: Array<File>, zipFile: File) {
    val parameters = ZipParameters()

    // 압축 메서드 선택 (STORE, DEFLATE 등)
    parameters.compressionMethod = CompressionMethod.DEFLATE

    // 암호화 설정 (선택사항)
    // parameters.isEncryptFiles = true
    // parameters.encryptionMethod = EncryptionMethod.ZIP_STANDARD
    // parameters.aesKeyStrength = EncryptionStrength.KEY_STRENGTH_256

    val zipFileObject = ZipFile(zipFile)
    fun tst() = zipFileObject.progressMonitor.percentDone
    for (file in files) {
        val targetPath = file.toRelativeString(files.first()) // 상대 경로 설정
        if (file.isFile) {
            zipFileObject.addFile(file, parameters)
        } else if (file.isDirectory) {
            zipFileObject.addFolder(file, parameters)
        }
    }
    println("파일 압축이 완료되었습니다.")
}



/** Todokanai
 *
 * 진행상황 callback만 미완성*/
@Throws(IOException::class)
fun zipWithoutCallback_td(zipFile:ZipFile, files:Array<File>){
    files.forEach { file ->
        if(file.isDirectory) {
            zipFile.addFolder(file)
        } else {
            zipFile.addFile(file)
        }
    }
}
/** Todokanai
 *
 * 진행상황 callback만 미완성
 * 대충 unzipHere에 해당?
 * */
@Throws(IOException::class)
fun unzipWithoutCallback_td(zipFile:ZipFile, target: Path) {
    val folder = File("${target.absolutePathString()}/${zipFile.file.nameWithoutExtension}")
    if(!folder.exists()) {
        println("creating folder ${folder.absolutePath}")
        folder.mkdir()
    }
    zipFile.extractAll(folder.toString())
}

/** Todokanai */
@Throws(IOException::class)
fun unzipHereWithoutCallback_td(zipFile:ZipFile, target: Path) {
    zipFile.extractAll(target.toString())
}

/** Todokanai */
@Throws(IOException::class)
fun zip4j_td() {
    // zip file with a single file
    ZipFile("filename.zip").addFile("file.txt")

    // zip file with multiple files
    val files = Arrays.asList(
        File("file1.txt"), File("file2.txt")
    )
    ZipFile("filename.zip").addFiles(files)

    // zip file with a folder
    ZipFile("filename.zip").addFolder(File("/home/mkyong/folder"))
}