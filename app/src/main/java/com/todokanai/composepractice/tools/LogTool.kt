package com.todokanai.composepractice.tools

import android.widget.Toast
import com.todokanai.composepractice.application.MyApplication
import com.todokanai.composepractice.tools.independent.HandlerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

/**
 *
 *  Log 기록 관련 method 모음
 */
class LogTool {
    private val myContext = MyApplication.appContext
    private val hModel = HandlerModel()

    fun makeShortToast(message:String) {
        hModel.myHandler({Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show()}, 0)
    }

    suspend fun printDispatcherInfo(){
        val thread = coroutineContext
        withContext(Dispatchers.Main){
            println("coroutineContext: $thread")
        }
    }      // 현재 Dispatcher 정보?
}