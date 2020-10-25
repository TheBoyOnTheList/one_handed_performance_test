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
            val layout: RelativeLayout = findViewById(R.id.`object`)//按钮所在布局的放缩
            val params: ViewGroup.LayoutParams = layout.layoutParams
            params.height = (params.height*1.1).toInt()
            params.width =  (params.width*1.1).toInt()
            layout.layoutParams = params

            val layout1: LinearLayout = findViewById(R.id.buttons)//五个按钮的放缩
            val params1: ViewGroup.LayoutParams = layout1.layoutParams
            params1.height = (params1.height*1.1).toInt()
            params1.width =  (params1.width*1.1).toInt()
            layout1.layoutParams = params1
        }

        narrow.setOnClickListener{
            val layout2: RelativeLayout = findViewById(R.id.`object`)//同上
            val params2: ViewGroup.LayoutParams = layout2.layoutParams
            params2.height = (params2.height*0.9).toInt()
            params2.width =  (params2.width*0.9).toInt()
            layout2.layoutParams = params2

            val layout3: LinearLayout = findViewById(R.id.buttons)
            val params3: ViewGroup.LayoutParams = layout3.layoutParams
            params3.height = (params3.height*0.9).toInt()
            params3.width =  (params3.width*0.9).toInt()
            layout3.layoutParams = params3
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        buttons.release()
    }
}