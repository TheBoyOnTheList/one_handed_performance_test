package com.example.one_handed_performance_test

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlin.concurrent.thread


class PlayActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        thread {
            while (true) {
                val text = findViewById<TextView>(R.id.textView)
                text.text = "Select:${MainActivity.select}   CM:${MainActivity.cm} ${MainActivity.cmOpr}   ZC:${MainActivity.zc} ${MainActivity.zcOpr}   " +
                        "TO:${MainActivity.to} ${MainActivity.toOpr}"
                Thread.sleep(100)
            }
        }
    }
}
