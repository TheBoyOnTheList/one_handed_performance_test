package com.example.one_handed_performance_test

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.one_handed_performance_test.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StrictMath.abs

class MainActivity : AppCompatActivity(),SensorEventListener {
    private val fromAlbum = 2
    private val scaleToBig = 1.1f
    private val scaleToSmall = 0.9f
    private var resmatrix: Matrix= getResMatrix()
    private var sensorManager: SensorManager? = null
    private lateinit var ima:ImageView
    private lateinit var showViewx: TextView
    private lateinit var showViewy: TextView
    private lateinit var showViewz: TextView
    private var accelerometerSensor: Sensor? = null
    private var gyroscopeSensor: Sensor? = null
    private var timestamp = 0f
    private var tmp = FloatArray(6)
    private var angle=FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //（调试用）屏幕上输出xyz的方向
        showViewx= findViewById(R.id.showTextx)
        showViewy= findViewById(R.id.showTexty)
        showViewz= findViewById(R.id.showTextz)

        ima=findViewById(R.id.imageView)
        //相册引入
        fromAlbumBtn.setOnClickListener{
            seLectPic()
        }

        //放大
        enlarge.setOnClickListener{
            enLarge()
        }

        //缩小
        narrow.setOnClickListener{
            naRrow()
        }
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscopeSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager!!.registerListener(this, gyroscopeSensor, android.hardware.SensorManager.SENSOR_DELAY_UI)
        sensorManager!!.registerListener(this, accelerometerSensor, android.hardware.SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event: SensorEvent){
        println("already enter onsensorchanged")
        if(ima.getTag().equals("selected")) {
            //println("alread enter second judgement")
            if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                println("so something wrong in here??")
//从 x、y、z 轴的正向位置观看处于原始方位的设备，如果设备逆时针旋转，将会收到正值；否则，为负值
                if (timestamp != 0f) {
                    //   println("timestamp something wrong?")
// 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
                    val dT = (event.timestamp - timestamp) * NS2S

// 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
                    angle[0] += event.values[0] * dT
                    angle[1] += event.values[1] * dT
                    angle[2] += event.values[2] * dT

// 将弧度转化为角度
                    var anglex = Math.toDegrees(angle[0].toDouble()).toFloat()
                    var angley = Math.toDegrees(angle[1].toDouble()).toFloat()
                    var anglez = Math.toDegrees(angle[2].toDouble()).toFloat()
                    //  println("anglex------------>$anglex")
                    //  println("angley------------>$angley")
                    //  println("anglez------------>$anglez")
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
                    if (tmp[3] > tmp[0] + 3) {
                        showViewx?.text = "x轴反向放大"
                        enLarge()
                    } else if (abs(tmp[3] - tmp[0]) < 0.1) {
                        showViewx?.text = "x轴不变"
                    } else if (tmp[3] < tmp[0] - 3) {
                        showViewx?.text = "x轴正向缩小"
                        naRrow()
                    }
                    if (tmp[4] > tmp[1] + 3) {
                        showViewy?.text = "y轴反向放大"
                        println("nothing wrong in y?")
                    } else if (abs(tmp[4] - tmp[1]) < 0.1) {
                        showViewy?.text = "y轴不变"
                    } else if (tmp[4] < tmp[1] - 3) {
                        showViewy?.text = "y轴正向缩小"
                    }
                    if (tmp[5] > tmp[2] + 3) {
                        showViewz?.text = "z轴反向放大"
                    } else if (abs(tmp[5] - tmp[2]) < 0.1) {
                        showViewz?.text = "z轴不变"
                    } else if (tmp[5] < tmp[2] - 3) {
                        showViewz?.text = "z轴正向缩小"
                    }
                }


//将当前时间赋值给timestamp
                timestamp = event.timestamp.toFloat()
            }
        }else{
            Toast.makeText(this,"Pic is unselected",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            fromAlbum->{
                if(resultCode== Activity.RESULT_OK&&data!=null){
                    data.data?.let{uri->
                        val bitmap=getBitmapFromUri(uri)
                        hiddenImg.setImageBitmap(bitmap)
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri)=contentResolver
            .openFileDescriptor(uri,"r")?.use{
                BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
            }
    private fun getResMatrix() = resmatrix

    private fun setResMatrix(imageView: ImageView) {
        resmatrix = imageView.imageMatrix
    }
    private fun enLarge(){
        val matrix = getResMatrix()
        matrix.postScale(scaleToBig, scaleToBig)
        val bitmapDrawable = hiddenImg.drawable as BitmapDrawable
        var bitmap = bitmapDrawable.bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true)
        imageView.setImageBitmap(bitmap)
    }
    private fun naRrow(){
        val matrix = getResMatrix()
        matrix.postScale(scaleToSmall, scaleToSmall)
        val bitmapDrawable = hiddenImg.drawable as BitmapDrawable
        var bitmap = bitmapDrawable.bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true)
        imageView.setImageBitmap(bitmap)
    }
    private fun seLectPic(){

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, fromAlbum)
        setResMatrix(imageView)
        ima.setTag("selected")


    }
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int){}
    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, gyroscopeSensor, android.hardware.SensorManager.SENSOR_DELAY_UI)
        sensorManager!!.registerListener(this, accelerometerSensor, android.hardware.SensorManager.SENSOR_DELAY_UI)
    }
    companion object {
        // 将纳秒转化为秒
        private const val NS2S = 1.0f / 1000000000.0f
    }

    override fun onDestroy() {
        super.onDestroy()
        buttons.release()
    }
}