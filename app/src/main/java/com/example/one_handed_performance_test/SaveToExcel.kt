package com.example.one_handed_performance_test

import jxl.write.WritableWorkbook
import java.io.File

class SaveToExcel(excelId: String) {
    private lateinit var wwb: WritableWorkbook
    private val excelFile: File = File(excelId)
    init {
        createExcel(excelFile)
    }
    private fun createExcel(file: File) {
    }
}