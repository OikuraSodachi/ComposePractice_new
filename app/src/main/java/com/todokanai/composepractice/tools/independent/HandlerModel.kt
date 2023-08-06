package com.todokanai.composepractice.tools.independent

import android.os.Handler
import android.os.Looper

/**
 * CoroutineScope에서 Main Thread 접근(?)을 위한 method
 * todokanai 의존성 없어야 함
 * */
class HandlerModel {

    fun myHandler(input:()->Unit,delayMills:Long){
        Handler(Looper.getMainLooper()).postDelayed({
            input()
        },delayMills)
    }

}