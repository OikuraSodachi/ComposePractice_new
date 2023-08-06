package com.todokanai.composepractice.tools

import java.io.File
import java.nio.file.Path

/** 테스트 용도 Class */
class TestClass {

    fun getDirectoryToUpdate(file:File): Path {

        return file.parentFile.toPath()
    }
}