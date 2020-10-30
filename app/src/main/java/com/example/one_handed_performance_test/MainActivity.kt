package com.example.one_handed_performance_test

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context


class MainActivity : AppCompatActivity(){
    
    private var x=0
    private var y=0
    private var z=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enlarge.setOnClickListener{
//            val layout: RelativeLayout = findViewById(R.id.`object`)//按钮所在布局的放缩，布局上的按钮随之放缩
//
//            layout.scaleX=(layout.scaleX)*1.1F//大小
//            layout.scaleY=(layout.scaleY)*1.1F

            z++
            if(z==2){
                z=0
                y++
            }
            if(y==5){
                y=0
                x++
            }
            if(x==4&&y==5&&z==2)println("finished")

            //全局变量to=a[x],zc=b[y],cm=c[z]，其中的数字随机，数字确定操作

            val n = (1..9).random()
            refresh(n)
        }

        narrow.setOnClickListener{
//            val layout: RelativeLayout = findViewById(R.id.`object`)//同上
//
//            layout.scaleX=(layout.scaleX)*0.9F
//            layout.scaleY=(layout.scaleY)*0.9F
        }

        /*right.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)

            layout.translationX=layout.translationX+100//针对图像左边，单位pixel
        }

        down.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)

            layout.translationY=layout.translationY+100//针对top(不知道是那个top)，单位pixel
        }

        leftUp.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)

            val newX= 0f//此处设置缩放中心
            val newY= 0f

            layout.translationX = layout.translationX + (layout.pivotX - newX) * (1 - layout.scaleX)
            layout.translationY = layout.translationY + (layout.pivotY - newY) * (1 - layout.scaleY)
            layout.pivotX=newX
            layout.pivotY=newY
        }

        rightDown.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)

            val newX= layout.width.toFloat()
            val newY= layout.height.toFloat()

            layout.translationX = layout.translationX + (layout.pivotX - newX) * (1 - layout.scaleX)
            layout.translationY = layout.translationY + (layout.pivotY - newY) * (1 - layout.scaleY)
            layout.pivotX=newX
            layout.pivotY=newY
        }*/

    }

    private fun Int.toPx():Int=(this* Resources.getSystem().displayMetrics.density).toInt() //px转dp

    private fun refresh(kind : Int) {//缩放布局的刷新和按钮的随机
        val parentLayout: LinearLayout = findViewById(R.id.parent)//父布局
        val oldLayout: RelativeLayout = findViewById(R.id.`object`)//控件
        parentLayout.removeView(oldLayout)//移除父布局中的旧控件

        val objectXml =layoutInflater.inflate(R.layout.`object`, null, false)//从文件中获取原始控件数据
        parentLayout.addView(objectXml)//此处注意，布局最外层控件的长宽不会被设置，需要手动设置

        val newLayout: RelativeLayout = findViewById(R.id.`object`)//刷新后的控件
        val paramsObject = newLayout.layoutParams
        paramsObject.height=300.toPx()
        paramsObject.width=300.toPx()
        newLayout.layoutParams=paramsObject

        val layoutButtons: LinearLayout = findViewById(R.id.buttons)//重新设置按钮的相对位置
        val paramsButtons: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(50.toPx(),50.toPx())

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

    }

    override fun onDestroy() {
        super.onDestroy()
        buttons.release()
    }
}