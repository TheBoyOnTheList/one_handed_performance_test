package com.example.one_handed_performance_test

import java.util.concurrent.LinkedBlockingDeque

class SaveDataRunnable(val saveToExcel: SaveToExcel, val queue: LinkedBlockingDeque<ExperimentData>): Runnable {
    override fun run() {
        while (true) {
            var experimentData: ExperimentData? = null
            try {
                experimentData = queue.take()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            if (experimentData?.feedback == -1)
                break;
            if (experimentData != null) {
                saveToExcel.save(experimentData)
            }
        }
    }
    fun stop() {
        try {
            queue.put(ExperimentData(0.0, 0.0, -1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}