package com.example.one_handed_performance_test

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_play.*
import java.util.*

class MainActivity : AppCompatActivity(){

    companion object{
        var select =0
        var block = 0
        var to = 0
        var zc = 0
        var cm = 0

        var toOpr = 0
        var zcOpr = 0
        var cmOpr = 0

        var TO: List<Int> = mutableListOf(1,2,3,4)//按照次序以此为，前后，左右，左上右下，右上左下
        var ZC: List<Int> = mutableListOf(1,2,3,4,5)//触点，触点上，触点下，触点外，中心点
        var CM: List<Int> = mutableListOf(1,2)//绝对映射，速率映射

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPlayActivity.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        }

        Collections.shuffle(ZC)
        Collections.shuffle(CM)

        toOpr = TO[0]
        zcOpr = ZC[0]
        cmOpr = CM[0]

    }

    override fun onDestroy() {
        super.onDestroy()
        changeableLayout.release()
    }

}
