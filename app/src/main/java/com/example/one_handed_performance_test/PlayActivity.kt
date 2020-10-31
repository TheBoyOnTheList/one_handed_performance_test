package com.example.one_handed_performance_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import kotlinx.android.synthetic.main.activity_play.*
import kotlin.concurrent.thread

class PlayActivity : AppCompatActivity() {

    private val refresh = 1
    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                refresh -> changeableLayout.refresh()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        for (x in 1..4) {
            thread {
                if(x==2)Thread.sleep(5000)
                while (true) {
                    if (changeableLayout.isClickTheTarget()) {
                        break
                    }
                }
                Log.d("hello", "${changeableLayout.isClickTheTarget()}")
                val msg = Message()
                msg.what = refresh
                handler.sendMessage(msg)
            }
        }
    }
}