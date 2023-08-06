package com.todokanai.composepractice.variables

import android.os.Environment
import com.todokanai.composepractice.myobjects.Constants.BY_DEFAULT
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_COPY
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_MOVE
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_UNZIP
import com.todokanai.composepractice.myobjects.Constants.CONFIRM_MODE_UNZIP_HERE
import com.todokanai.composepractice.myobjects.Constants.DEFAULT_MODE
import com.todokanai.composepractice.myobjects.Constants.MULTI_SELECT_MODE
import com.todokanai.composepractice.tools.independent.dirTree_td
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class Variables {
    companion object{
        private val defaultStorage = Environment.getExternalStorageDirectory()

        private val _physicalStorageList = MutableStateFlow<List<File>>(listOf(defaultStorage))       // 물리적 저장소 목록
        val physicalStorageList : StateFlow<List<File>>
            get() = _physicalStorageList
        /**
         * 파일 복사/압축 등 작업 도중에 currentPath 변경이 발생할 경우, 작업 완료후 자동 업데이트가 안되고 있음
         */
        private val _currentFileList = MutableStateFlow<List<File>>(emptyList())
        val currentFileList : StateFlow<List<File>>
            get() = _currentFileList

        private val _dirTree = MutableStateFlow<List<File>>(dirTree_td(defaultStorage))     // 초기값은 최초 화면 디렉토리로
        val dirTree : StateFlow<List<File>>
            get() = _dirTree

        private val _currentPath = MutableStateFlow<File>(defaultStorage)
        val currentPath : StateFlow<File>
            get() = _currentPath

        private val _selectMode = MutableStateFlow<Int>(DEFAULT_MODE)
        val selectMode : StateFlow<Int>
            get() = _selectMode

        private val _selectedList = MutableStateFlow<List<File>>(emptyList())
        val selectedList : StateFlow<List<File>>
            get() = _selectedList

        private val _sortMode = MutableStateFlow<String>(BY_DEFAULT)
        val sortMode : StateFlow<String>
            get() = _sortMode
    }
    //--------------------------------------------------------
    // setter 구간. 전부 private

    private fun setPhysicalStorageList(storageList:List<File>){
        _physicalStorageList.value = storageList
    }

    private fun setSortMode(sortMode:String){
        _sortMode.value = sortMode
    }

    /** dirTree값은 항상 currentPath에 묶여다님
     *
     * currentPath 변경시 따라다니는 변경값: dirTree, currentFileList
     */

    private fun setCurrentPath(file:File){
        _currentPath.value = file
        _dirTree.value = dirTree_td(file)
        setCurrentFileList()
    }

    private fun setSelectMode(selectMode: Int){
        _selectMode.value = selectMode
    }

    private fun setSelectedList(selectedList:List<File>){
        _selectedList.value = selectedList
    }

    /** sortMode 적용한 값을 set 하도록 변경할것 !!!!! */
    private fun setCurrentFileList() {
        currentPath.value.listFiles()?.let { files ->
            _currentFileList.value = FileListSorter().sortFileList(sortMode.value,files)
        }
    }
    // setter 구간 끝
    //---------------------------------

    fun updatePhysicalStorageList(storageList:List<File>) {
        setPhysicalStorageList(storageList)
    }

    /** currentFileList 값 재정렬 동작이 포함됨 */
    private fun updateSortMode(sortMode:String) {
        setSortMode(sortMode)
        setCurrentFileList()
    }

    /** CurrentPath 값의 변화가 있을떄
     *
     * path가 접근 가능할 경우에만 setter 호출
     */
    fun updateCurrentPath(file:File)  {
        file.listFiles()?.let {
            setCurrentPath(file)
        }
    }

    fun updateSelectMode(selectMode:Int){
        setSelectMode(selectMode)                 // selectMode 값 변경
        when(selectMode){
            DEFAULT_MODE ->{
                setSelectedList(emptyList())
            }
            MULTI_SELECT_MODE ->{

            }
            CONFIRM_MODE_MOVE ->{

            }
            CONFIRM_MODE_COPY ->{

            }
            CONFIRM_MODE_UNZIP ->{

            }
            CONFIRM_MODE_UNZIP_HERE ->{

            }
        }                                               // 각 Mode마다 추가 메소드 구간
    }                                                     // selectMode 값 관리

    fun toggleSelection(file:File){
        val newList = listToggle(file,selectedList.value.toList())
        setSelectedList(newList)
    }                                                           // file 선택에 대해서 selectedList 업데이트

    private fun listToggle(file:File, files:List<File>):List<File>{
        return if(files.contains(file)){
            files.minus(file)
        } else{
            files.plus(file)
        }
    }

    private fun getSortModeCallbackList(contents:List<String>, callback:(text:String)->Unit):List<Pair<String,()->Unit>>{
        val result = mutableListOf<Pair<String,()->Unit>>()
        contents.forEach { text ->
            result.add(Pair(text,{callback(text)}))
        }
        return result
    }

    fun sortModeCallbackList() = getSortModeCallbackList(FileListSorter.sortModeListOriginal,{updateSortMode(it)})
}