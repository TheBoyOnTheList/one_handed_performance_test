package com.example.one_handed_performance_test

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_play.*
import java.util.*
import kotlin.math.abs

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

        var transX=0F
        var transY=0F

        val toName = arrayOf("前后","左右","左上右下","右上左下")
        val zcName = arrayOf("触点","触点上","触点下","触点外","中心点")
        val cmName = arrayOf("绝对映射","速率映射")

        var leftORrightHand = 1

        val subjectID = 1 //受试者编号
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPlayActivity.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        }
        leftORright.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("select hand")
                setMessage("请选择您的惯用手")
                setCancelable(false)
                setPositiveButton("右手"){
                        dialog,which->
                    leftORrightHand=1
                }
                setNegativeButton("左手"){
                        dialog,which->
                    leftORrightHand=2
                }
                show()
            }
        }
        Collections.shuffle(ZC)
        Collections.shuffle(CM)

        toOpr=TO[0]
        zcOpr=ZC[0]
        cmOpr=CM[0]
    }

    override fun onDestroy() {
        super.onDestroy()
        changeableLayout.release()
    }

}
