package com.example.one_handed_performance_test

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.button_array.view.*

import com.example.one_handed_performance_test.MainActivity.Companion.to//计数
import com.example.one_handed_performance_test.MainActivity.Companion.zc
import com.example.one_handed_performance_test.MainActivity.Companion.cm
import com.example.one_handed_performance_test.MainActivity.Companion.arr//数组

class ButtonArray(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs),AppCompatActivity(){
    private val errorAudioPlayer = MediaPlayer()//播放音频对象
    private val rightAudioPlayer = MediaPlayer()
    private var error = 0


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

                cm++
                if(cm==2){
                    cm=0
                    zc++
                }
                if(zc==5){
                    zc=0
                    to++
                }
                if(to==4&&zc==5&&cm==2)println("finished")//toast

                val t = MainActivity()
                val n = (1..9).random()
                t.refresh(n)
            }
        }
        else {
            bt.setOnClickListener {
                bt.setBackgroundResource(R.drawable.shape_circle_red)
                errorAudioPlayer.start()
                error++
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

    fun getError() = error

//    private fun refresh(kind : Int) {//缩放布局的刷新和按钮的随机
//
//        val parentLayout: LinearLayout = findViewById(R.id.parent)//父布局
//        val oldLayout: RelativeLayout = findViewById(R.id.`object`)//控件
//        parentLayout.removeView(oldLayout)//移除父布局中的旧控件
//
//
//        val objectXml = LayoutInflater.from(context).inflate(R.layout.`object`, null, false)//从文件中获取原始控件数据
//        parentLayout.addView(objectXml)//此处注意，布局最外层控件的长宽不会被设置，需要手动设置
//
//        val newLayout: RelativeLayout = findViewById(R.id.`object`)//刷新后的控件
//        val paramsObject = newLayout.layoutParams
//        paramsObject.height=300.toPx()
//        paramsObject.width=300.toPx()
//        newLayout.layoutParams=paramsObject
//
//        val layoutButtons: LinearLayout = findViewById(R.id.buttons)//重新设置按钮的相对位置
//        val paramsButtons: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(50.toPx(),50.toPx())
//
//        when(kind){//选择按钮更新位置
//            1 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_LEFT)//左上角
//            2 -> paramsButtons.addRule(RelativeLayout.CENTER_HORIZONTAL)//上边正中
//            3 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)//右上角
//            4 -> paramsButtons.addRule(RelativeLayout.CENTER_VERTICAL)//左边正中
//            5 -> paramsButtons.addRule(RelativeLayout.CENTER_IN_PARENT)//中心
//            6 -> {
//                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)//右边正中
//                paramsButtons.addRule(RelativeLayout.CENTER_VERTICAL)
//            }
//            7 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)//左下角
//            8 -> {
//                paramsButtons.addRule(RelativeLayout.CENTER_HORIZONTAL)//下边正中
//                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//            }
//            9 -> {
//                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)//右下角
//                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//            }
//        }
//
//        layoutButtons.layoutParams=paramsButtons//重绘
//
//    }
//
//    private fun Int.toPx():Int=(this* Resources.getSystem().displayMetrics.density).toInt() //px转dp
}