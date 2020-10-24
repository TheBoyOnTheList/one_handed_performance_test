package com.example.one_handed_performance_test

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.button_array.view.*

class ButtonArray(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs){
    private val errorAudioPlayer = MediaPlayer()//播放音频对象
    private val rightAudioPlayer = MediaPlayer()
    init {
        LayoutInflater.from(context).inflate(R.layout.button_array, this)
        Log.d("createButtons", "here1")
        initMediaPlayer()
        setButtonListener()
    }
    private fun setButtonListener() {
        val buttonList = listOf(button_0, button_1, button_2, button_3, button_4)
        for (bt in buttonList) {
            setEachButtonListener(bt)
        }
    }
    private fun setEachButtonListener(bt: Button) {
        if (bt == button_2) {
            bt.setOnClickListener {
                bt.setBackgroundResource(R.drawable.shape_circle_green)
                rightAudioPlayer.start()
            }
        }
        else {
            bt.setOnClickListener {
                bt.setBackgroundResource(R.drawable.shape_circle_red)
                errorAudioPlayer.start()
            }
        }
    }
    private fun initMediaPlayer() {
        val assetManager = context.assets
        val error = assetManager.openFd("error.mp3")
        val right = assetManager.openFd("right.mp3")
        errorAudioPlayer.setDataSource(error.fileDescriptor, error.startOffset, error.length)
        errorAudioPlayer.prepare()
        rightAudioPlayer.setDataSource(right.fileDescriptor, right.startOffset, right.length)
        rightAudioPlayer.prepare()
    }
    fun release() {
        errorAudioPlayer.stop()
        errorAudioPlayer.release()
        rightAudioPlayer.stop()
        rightAudioPlayer.release()
    }
}