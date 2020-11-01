package com.example.one_handed_performance_test

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.button_array.view.*

class ChangeableLayout(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {
    private val errorAudioPlayer = MediaPlayer()//播放音频对象
    private val rightAudioPlayer = MediaPlayer()
    private var buttonList: List<Button>//按钮列表
    init {
        LayoutInflater.from(context).inflate(R.layout.changeable_layout, this)
        initMediaPlayer()
        buttonList = listOf(button_0, button_1, button_2, button_3, button_4)
        setButtonListener()
    }

    //设置每个按钮的点击事件
    private fun setButtonListener() {
        for (bt in buttonList) {
            setEachButtonListener(bt)
        }
    }

    //设置按钮的点击事件
    private fun setEachButtonListener(bt: Button) {
        if (bt == button_2) {
            bt.setOnClickListener {
                bt.setBackgroundResource(R.drawable.shape_circle_green)
                rightAudioPlayer.start()
                MainActivity.cm++
                if(MainActivity.cm ==2){
                    MainActivity.cm =0
                    MainActivity.zc++
                }
                if(MainActivity.zc ==5){
                    MainActivity.zc =0
                    MainActivity.to++
                }
                if(MainActivity.to==4){
                    MainActivity.to=0
                    MainActivity.block++
                }
                if(MainActivity.block==3&&MainActivity.to ==4&& MainActivity.zc ==5&& MainActivity.cm ==2)
                    Toast.makeText(context, "This is the last ont!", Toast.LENGTH_SHORT).show()
                layoutRefresh()
            }
        }
        else {
            bt.setOnClickListener {
                bt.setBackgroundResource(R.drawable.shape_circle_red)
                errorAudioPlayer.start()
            }
        }
    }

    //初始化音频资源
    private fun initMediaPlayer() {
        val assetManager = context.assets
        val error = assetManager.openFd("error.mp3")
        val right = assetManager.openFd("right.mp3")
        errorAudioPlayer.setDataSource(error.fileDescriptor, error.startOffset, error.length)
        errorAudioPlayer.prepare()
        rightAudioPlayer.setDataSource(right.fileDescriptor, right.startOffset, right.length)
        rightAudioPlayer.prepare()
    }

    //刷新按钮
    fun buttonsRefresh() {
        for (bt in buttonList) {
            if (bt == button_2)
                bt.setBackgroundResource(R.drawable.shape_circle_blue)
            else
                bt.setBackgroundResource(R.drawable.shape_circle_grey)
        }
    }

    //刷新布局位置，同时改变按钮方位
    fun layoutRefresh() {
//        this.translationX = 10f.toPxFloat()
//        this.translationY = 10f.toPxFloat()
        val layoutButtons: LinearLayout = findViewById(R.id.buttons)//重新设置按钮的相对位置
        val paramsButtons: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(50.toPxInt(),50.toPxInt())
        val kind = (1..9).random()
        when(kind){//选择按钮更新位置
            1 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_LEFT)//左上角
            2 -> paramsButtons.addRule(RelativeLayout.CENTER_HORIZONTAL)//上边正中
            3 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)//右上角
            4 -> paramsButtons.addRule(RelativeLayout.CENTER_VERTICAL)//左边正中
            5 -> paramsButtons.addRule(RelativeLayout.CENTER_IN_PARENT)//中心
            6 -> {
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)//右边正中
                paramsButtons.addRule(RelativeLayout.CENTER_VERTICAL)
            }
            7 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)//左下角
            8 -> {
                paramsButtons.addRule(RelativeLayout.CENTER_HORIZONTAL)//下边正中
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
            9 -> {
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)//右下角
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            }
        }
        layoutButtons.layoutParams=paramsButtons//重绘
        buttonsRefresh()
    }

    private fun Int.toPxInt():Int=(this* Resources.getSystem().displayMetrics.density).toInt()
    private fun Float.toPxFloat():Float=(this* Resources.getSystem().displayMetrics.density).toFloat()

    //设置布局的位置
    fun setLocation(x: Float, y:Float) {

    }

    //设置布局的大小
    fun setSizeOfLayout() {

    }

    //释放音频资源
    fun release() {
        errorAudioPlayer.stop()
        errorAudioPlayer.release()
        rightAudioPlayer.stop()
        rightAudioPlayer.release()
    }
}