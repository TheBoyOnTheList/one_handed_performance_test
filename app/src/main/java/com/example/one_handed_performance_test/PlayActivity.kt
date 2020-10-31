package com.example.one_handed_performance_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import kotlinx.android.synthetic.main.activity_play.*
import kotlin.concurrent.thread

class PlayActivity : AppCompatActivity() {
    private var flag = true
    private val refresh = 1
    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                refresh -> {
                    changeableLayout.refresh()
                    flag = false
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        thread {
            while (true) {
                while (true) {
                    if (changeableLayout.isClickTheTarget()) {
                        break
                    }
                }
                val msg = Message()
                msg.what = refresh
                handler.sendMessage(msg)
                while (flag) {}
            }
        }
    }
}
