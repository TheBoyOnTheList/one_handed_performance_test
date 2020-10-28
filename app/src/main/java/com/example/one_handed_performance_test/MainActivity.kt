package com.example.one_handed_performance_test

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enlarge.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)//按钮所在布局的放缩，布局上的按钮随之放缩

            layout.scaleX=(layout.scaleX)*1.1F//大小
            layout.scaleY=(layout.scaleY)*1.1F
        }

        narrow.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)//同上

            layout.scaleX=(layout.scaleX)*0.9F
            layout.scaleY=(layout.scaleY)*0.9F
        }

        /*right.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)

            layout.translationX=layout.translationX+100//针对图像左边，单位pixel
        }

        down.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)

            layout.translationY=layout.translationY+100//针对top(不知道是那个top)，单位pixel
        }*/

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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        buttons.release()
    }
}