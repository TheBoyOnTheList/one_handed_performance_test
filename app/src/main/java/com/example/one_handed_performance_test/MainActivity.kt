package com.example.one_handed_performance_test

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enlarge.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)//按钮所在布局的放缩，布局上的按钮随之放缩

            layout.pivotX=100F;//放缩中心，(0,0)为。。老实说我也不太懂它的单位与方式。
            layout.pivotY=100F;

            layout.scaleX=(layout.scaleX)*1.1F;//大小
            layout.scaleY=(layout.scaleY)*1.1F;
        }

        narrow.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)//同上

            layout.pivotX= 300F;
            layout.pivotY= 300F;

            layout.scaleX=(layout.scaleX)*0.9F;
            layout.scaleY=(layout.scaleY)*0.9F;
        }

        right.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)

            layout.translationX=layout.translationX+100;//针对图像左边，单位pixel
        }

        down.setOnClickListener{
            val layout: RelativeLayout = findViewById(R.id.`object`)

            layout.translationY=layout.translationY+100;//针对top(不知道是那个top)，单位pixel
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        buttons.release()
    }
}