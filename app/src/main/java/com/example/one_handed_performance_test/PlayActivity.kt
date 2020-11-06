package com.example.one_handed_performance_test

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.one_handed_performance_test.MainActivity.Companion.select

import kotlinx.android.synthetic.main.activity_play.*
import kotlin.concurrent.thread
import kotlin.math.abs

class PlayActivity : AppCompatActivity(),SensorEventListener {
    //private var timestamp0 = 0f
    //private var timestamp1=0f
    //private var timestamp2=0f


    private var tmp = FloatArray(6)
    private var angle=FloatArray(3)
    private var sensorManager: SensorManager? = null
    private var accelerometerSensor: Sensor? = null
    private var gyroscopeSensor: Sensor? = null
    companion object {
        //计时变量
        var To = LongArray(16){0L}
        var Ti = LongArray(16){0L}
        var Ts = LongArray(16){0L}
        var tmpt =0L
        var timestamp=FloatArray(3){0f}
        //判断触摸及运行状态的
        var flag = 0
        var lockdown = 0

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val layout: RelativeLayout = findViewById(R.id.changeableLayout)
        MainActivity.transX = layout.translationX
        MainActivity.transY = layout.translationY

        thread {
            while (true) {
                val text = findViewById<TextView>(R.id.textView)
                text.text =
                    "Select:${MainActivity.select}   CM:${MainActivity.cmOpr},${MainActivity.cmName}   ZC: ${MainActivity.zcOpr},${MainActivity.zcName}   " +
                            "TO:${MainActivity.toOpr},${MainActivity.toName}"
                Thread.sleep(100)
            }
        }

        thread {
            while (true) {
                if (MainActivity.select == 16) {
                    MainActivity.select = 0
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
            }
        }

        /* enlarge.setOnClickListener{
             val layout: RelativeLayout = findViewById(R.id.changeableLayout)//按钮所在布局的放缩，布局上的按钮随之放缩

             layout.scaleX=(layout.scaleX)*1.1F//大小
             layout.scaleY=(layout.scaleY)*1.1F

         }

         narrow.setOnClickListener{
             val layout: RelativeLayout = findViewById(R.id.changeableLayout)//同上

             layout.scaleX=(layout.scaleX)*0.9F
             layout.scaleY=(layout.scaleY)*0.9F
         }

         right.setOnClickListener{
             val layout: RelativeLayout = findViewById(R.id.changeableLayout)

             layout.translationX=layout.translationX+100//针对图像左边，单位pixel
         }

         down.setOnClickListener{
             val layout: RelativeLayout = findViewById(R.id.changeableLayout)

             layout.translationY=layout.translationY+100//针对top(不知道是那个top)，单位pixel
         }

         leftUp.setOnClickListener{
             val layout: RelativeLayout = findViewById(R.id.changeableLayout)

             val newX= 0f//此处设置缩放中心
             val newY= 0f

             layout.translationX = layout.translationX + (layout.pivotX - newX) * (1 - layout.scaleX)
             layout.translationY = layout.translationY + (layout.pivotY - newY) * (1 - layout.scaleY)
             layout.pivotX=newX
             layout.pivotY=newY
         }

         rightDown.setOnClickListener{
             val layout: RelativeLayout = findViewById(R.id.changeableLayout)

             val newX= layout.width.toFloat()
             val newY= layout.height.toFloat()

             layout.translationX = layout.translationX + (layout.pivotX - newX) * (1 - layout.scaleX)
             layout.translationY = layout.translationY + (layout.pivotY - newY) * (1 - layout.scaleY)
             layout.pivotX=newX
             layout.pivotY=newY
         }*/
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscopeSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager!!.registerListener(
            this,
            gyroscopeSensor,
            android.hardware.SensorManager.SENSOR_DELAY_GAME
        )
        sensorManager!!.registerListener(
            this,
            accelerometerSensor,
            android.hardware.SensorManager.SENSOR_DELAY_NORMAL
        )
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var Flaag=0 //开关变量
        when(event?.actionMasked){
            MotionEvent.ACTION_DOWN -> {


                if (Ti[select] != 0L && Ts[select] == 0L) {
                    Ts[select] = System.currentTimeMillis() - tmpt
                    println("三Ts时间为" + Ts)
                    Log.d("手指", "onTouchEvent: ts成功开始")        //如果是记录碰到手机而不是准确落在点上时，这段就可以留下，不然就可以删了
                    changeableLayout.layoutRefresh()
                    changeableLayout.iNit()
                    flag=-1

                }else {
                    flag = 0
                    Log.d("手指", "onTouchEvent: 落下但Ts未开始")
                    tmpt = System.currentTimeMillis()
                    lockdown = 1
                }

            }
            MotionEvent.ACTION_MOVE->{
                flag =0
            }
            MotionEvent.ACTION_UP->{
                if(To[select] !=0L&& Ts[select] ==0L){
                    Ti[select] =System.currentTimeMillis()- tmpt
                    println("二Ti时间为"+ Ti)
                    flag =1
                    println("finger up")
                }
                if(Ti[select] ==0L&&flag!=-1) {
                    To[select] = System.currentTimeMillis() - tmpt
                    tmpt = System.currentTimeMillis()
                    println("一To时间为" + To)
                    flag =1
                }

            }
        }
        return super.onTouchEvent(event)
    }
    override fun onSensorChanged(event: SensorEvent){
        if(flag ==1&& Ti[select] ==0L) {    //平移阶段开始工作

            if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                if (timestamp[0] != 0f) {
                    val NS2S = 1.0f / 1000000000.0f
                    val dT = (event.timestamp - timestamp[0]) * NS2S
                    angle[0] += event.values[0] * dT
                    angle[1] += event.values[1] * dT
                    var anglex = Math.toDegrees(angle[0].toDouble()).toFloat()
                    var angley = Math.toDegrees(angle[1].toDouble()).toFloat()
                    /*    if (abs(anglex) < 2 ) {            //“降噪”消除小抖动带来的影响，仍不是很确定要不要采用
                            anglex = 0F

                        }
                        if( abs(angley) < 2 ){
                            angley = 0F
                        }*/
                    println("进入平移阶段")
                    Log.d("平移标记", " Ti: "+Ti+" lockdown= "+lockdown+" flag:"+flag)
                    if(abs(anglex)<abs(angley)) {
                        horizentalShift(angley)
                    }else if(abs(anglex)>abs(angley)) {
                        verticalShift(anglex)
                    }
                }
                timestamp[2]=0f
                timestamp[0]=event.timestamp.toFloat()
            }
            return //保证平移时只会进行平移的操作

        }
        if(Ti[select]!=0L|| lockdown==0){
            return
        } //经过平移和放大后，让陀螺仪停止工作

        //MainActivity.cm =2  //临时测试给一个数据，仅用绝对映射方式
        when(MainActivity.cmOpr)  {
            1-> {
                if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                    println("so something wrong in here??")
//从 x、y、z 轴的正向位置观看处于原始方位的设备，如果设备逆时针旋转，将会收到正值；否则，为负值
                    if (timestamp[1] != 0f) {
// 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
                        val NS2S = 1.0f / 1000000000.0f
                        val dT = (event.timestamp - timestamp[1]) * NS2S

// 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
                        angle[0] += event.values[0] * dT
                        angle[1] += event.values[1] * dT
                        angle[2] += event.values[2] * dT

// 将弧度转化为角度
                        var anglex = Math.toDegrees(angle[0].toDouble()).toFloat()
                        var angley = Math.toDegrees(angle[1].toDouble()).toFloat()
                        var anglez = Math.toDegrees(angle[2].toDouble()).toFloat()
                        println("anglex------------>$anglex")
                        println("angley------------>$angley")
                        println("anglez------------>$anglez")
                        /*   if (abs(anglex) < 2  ) {
                               anglex = 0F
                           }
                           if(abs(angley) < 2){
                               angley=0F
                           }
                           */

                        println("gyroscopeSensor.getMinDelay()----------->" + gyroscopeSensor!!.minDelay)

                        tmp[3] = tmp[0]
                        tmp[4] = tmp[1]
                        tmp[5] = tmp[2]
                        tmp[0] = anglex
                        tmp[1] = angley
                        tmp[2] = anglez
                        println("start output")
                        for (i in 0..5) {
                            println(tmp[i])

                        }
                        //x,y反向放大，正向缩小，右下、左下缩小，左上、右上放大
                        //  MainActivity.to=1//临时测试给一个数据，仅用x轴进行放缩
                        when (MainActivity.toOpr) {
                            1-> if (tmp[3] > tmp[0] + 3) {                //纯x轴，即前后
                                // showViewx?.text = "x轴反向放大"
                                enLarge(0F, 0F, 0F, 1)
                            }
                            /*
                            else if (StrictMath.abs(tmp[3] - tmp[0]) < 0.1) {
                                //  showViewx?.text = "x轴不变"
                            }
    */
                            else if (tmp[3] < tmp[0] - 3) {
                                // showViewx?.text = "x轴正向缩小"
                                naRrow(0F, 0F, 0F, 1)
                            }
                            2-> if (tmp[4] > tmp[1] + 3) {               //纯y轴，即左右
                                //  showViewy?.text = "y轴反向放大"
                                enLarge(0F, 0F, 0F, 1)
                            }
/*
                        else if (StrictMath.abs(tmp[4] - tmp[1]) < 0.1) {
                            //  showViewy?.text = "y轴不变"
                         }
 */
                            else if (tmp[4] < tmp[1] - 3) {
                                naRrow(0F, 0F, 0F, 1)
                                // showViewy?.text = "y轴正向缩小"
                            }
                            3-> if (tmp[5] > tmp[2] - 0.1) {
                                //showViewz?.text = "z轴反向放大"
                                //保证z轴不是正向缩小即可，因为感知不是很强
                                if (tmp[3] > tmp[0] + 3 && tmp[4] > tmp[1] + 3) {
                                    enLarge(0F, 0F, 0F, 1)
                                } else if (tmp[3] < tmp[0] - 3 && tmp[4] < tmp[1] - 3) {
                                    naRrow(0F, 0F, 0F, 1)
                                }
                            }
                            4->  if (tmp[5] < tmp[2] + 0.1) {
                                //  showViewz?.text = "z轴正向缩小"
                                //道理同上
                                if (tmp[3] > tmp[0] + 3 && tmp[4] < tmp[1] - 3) {
                                    enLarge(0F, 0F, 0F, 1)
                                } else if (tmp[3] < tmp[0] - 3 && tmp[4] > tmp[1] + 3) {
                                    naRrow(0F, 0F, 0F, 1)
                                }
                            }
                        }
                    }

//将当前时间赋值给timestamp
                    timestamp[1] = event.timestamp.toFloat()
                }
            }
            2->{
                if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
//从 x、y、z 轴的正向位置观看处于原始方位的设备，如果设备逆时针旋转，将会收到正值；否则，为负值
                    if (timestamp[2] != 0f) {
// 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
                        val NS2S = 1.0f / 1000000000.0f
                        val dT = (event.timestamp - timestamp[2]) * NS2S

// 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
                        angle[0] += event.values[0] * dT
                        angle[1] += event.values[1] * dT
                        angle[2] += event.values[2] * dT

// 将弧度转化为角度
                        var anglex =angle[0]+1// Math.toDegrees(angle[0].toDouble()).toFloat()
                        var angley =angle[1]+1// Math.toDegrees(angle[1].toDouble()).toFloat()
                        var anglez =angle[2]+1// Math.toDegrees(angle[2].toDouble()).toFloat()
                        println("anglex------------>$anglex")
                        println("angley------------>$angley")
                        println("anglez------------>$anglez")
                        /*   if(abs(anglex)<7&&abs(angley)<7){
                               anglex=0F
                               angley=0F
                           }*/
                        if((anglex>0&&angley>0&&anglez<0)||(anglex>0&&angley<0&&anglez>0)||angley>0||anglex>0) {
                            enLarge(anglex, angley, anglez,2)
                        }
                        if((anglex<0&&angley<0&&anglez<0)||(anglex<0&&angley>0&&anglez>0)||angley<0||anglex<0) {
                            naRrow(anglex, angley, anglez,2)
                        }

                        println("gyroscopeSensor.getMinDelay()----------->" + gyroscopeSensor!!.minDelay)


                    }
//将当前时间赋值给timestamp
                    timestamp[2] = event.timestamp.toFloat()
                }

            }
            0-> println("something wrong")
            3-> println("do nothing")
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        sensorManager!!.unregisterListener(this)
    }
    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, gyroscopeSensor, android.hardware.SensorManager.SENSOR_DELAY_UI)
        sensorManager!!.registerListener(this, accelerometerSensor, android.hardware.SensorManager.SENSOR_DELAY_UI)
    }
    fun enLarge(tmpx: Float,tmpy:Float,tmpz:Float,Fla:Int ){
        val layout: RelativeLayout = findViewById(R.id.changeableLayout)//按钮所在布局的放缩，布局上的按钮随之放缩

        if(Fla==1) {

            layout.scaleX = (layout.scaleX) * 1.1F;//大小
            layout.scaleY = (layout.scaleY) * 1.1F;
        }else{
            layout.scaleX = (layout.scaleX) * 1.005F*abs(tmpx)//*abs(tmpy)*abs(tmpz);//大小
            layout.scaleY = (layout.scaleY) * 1.005F*abs(tmpx)//*abs(tmpy)*abs(tmpz);
        }
    }
    fun naRrow(tmpx: Float,tmpy:Float,tmpz:Float,Fla:Int){
        val layout: RelativeLayout = findViewById(R.id.changeableLayout)//同上


        if(Fla==1) {
            layout.scaleX = (layout.scaleX) * 0.9F;
            layout.scaleY = (layout.scaleY) * 0.9F;
        }else{
            layout.scaleX=(layout.scaleX)*0.995F*abs(tmpx)//*abs(tmpy)*abs(tmpz);
            layout.scaleY=(layout.scaleY)*0.995F*abs(tmpx)//*abs(tmpy)*abs(tmpz);
        }
    }
    fun horizentalShift(ang:Float){
        println("水平平移阶段")
        val layout: RelativeLayout = findViewById(R.id.changeableLayout)
        if(ang>0) {
            layout.translationX = layout.translationX - 10//针对图像左边，单位pixel
        }else if(ang<0){
            layout.translationX = layout.translationX + 10
        }
    }
    fun verticalShift(ang:Float){
        println("垂直平移阶段")
        val layout: RelativeLayout = findViewById(R.id.changeableLayout)
        if(ang<0) {
            layout.translationY = layout.translationY + 10//针对top(不知道是那个top)，单位pixel
        }else if(ang>0){
            layout.translationY = layout.translationY - 10
        }
    }


}
