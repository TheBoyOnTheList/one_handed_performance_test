package com.example.one_handed_performance_test

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.one_handed_performance_test.MainActivity.Companion.select
import com.example.one_handed_performance_test.PlayActivity.Companion.Ti
import com.example.one_handed_performance_test.PlayActivity.Companion.To
import com.example.one_handed_performance_test.PlayActivity.Companion.Ts
import com.example.one_handed_performance_test.PlayActivity.Companion.angle
import com.example.one_handed_performance_test.PlayActivity.Companion.flag
import com.example.one_handed_performance_test.PlayActivity.Companion.lockdown
import com.example.one_handed_performance_test.PlayActivity.Companion.timestamp
import com.example.one_handed_performance_test.PlayActivity.Companion.tmpt
import kotlinx.android.synthetic.main.button_array.view.*
import java.io.File
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.log10


class ChangeableLayout(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {
    private val errorAudioPlayer = MediaPlayer()//播放音频对象
    private val rightAudioPlayer = MediaPlayer()
    private var buttonList: List<Button>//按钮列表

    //向excel存数据使用到的变量
    private lateinit var dataList: LinkedBlockingDeque<ExperimentData>
    private lateinit var subjectInfo: String
    private lateinit var saveToExcel: SaveToExcel
    private lateinit var runnable: SaveDataRunnable
    init {
        LayoutInflater.from(context).inflate(R.layout.changeable_layout, this)
        initMediaPlayer()
        subjectInfo = "data"
        initSaveDataMethod()
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
    @SuppressLint("SetTextI18n")
    private fun setEachButtonListener(bt: Button) {
        if (bt == button_2) {
            bt.setOnClickListener {
                if (Ti[select] != 0L ) {
                    Ts[select] = System.currentTimeMillis() - tmpt
                    println("三Ts时间为" + Ts)
                    Log.d("手指", "subPlayChanged: ts成功开始")
                }
                bt.setBackgroundResource(R.drawable.shape_circle_green)
                rightAudioPlayer.start()

                MainActivity.select++
                if (MainActivity.select==16){
//                    MainActivity.select=0
                    MainActivity.cm++
                }
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
                    Collections.shuffle(MainActivity.ZC)
                    Collections.shuffle(MainActivity.CM)
                }
                if(MainActivity.block==2&&MainActivity.to ==3&& MainActivity.zc ==4&& MainActivity.cm ==1)
                    Toast.makeText(context, "This is the last ont!", Toast.LENGTH_SHORT).show()
                MainActivity.toOpr = MainActivity.TO[MainActivity.to]
                MainActivity.zcOpr = MainActivity.ZC[MainActivity.zc]
                MainActivity.cmOpr = MainActivity.CM[MainActivity.cm]
                layoutRefresh()
                taskCompleted(0)
                iNit()
            }
        }
        else {
            bt.setOnClickListener {
                bt.setBackgroundResource(R.drawable.shape_circle_red)
                errorAudioPlayer.start()
            }
            layoutRefresh()
            taskCompleted(1)
            iNit()
            flag=-1
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

        val layout: RelativeLayout = findViewById(R.id.changeableLayout)//刷新后的控件
        layout.scaleX=1F
        layout.scaleY=1F
        layout.translationX = MainActivity.transX-300
        layout.translationY = MainActivity.transY-300
        val layoutButtons: LinearLayout = findViewById(R.id.buttons)//重新设置按钮的相对位置
        val paramsButtons: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(100.toPxInt(),100.toPxInt())

        val kind = (1..8).random()
        when(kind){//选择按钮更新位置
            1 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_LEFT)//左上角
            2 -> paramsButtons.addRule(RelativeLayout.CENTER_HORIZONTAL)//上边正中
            3 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)//右上角
            4 -> paramsButtons.addRule(RelativeLayout.CENTER_VERTICAL)//左边正中
            5 -> {
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)//右边正中
                paramsButtons.addRule(RelativeLayout.CENTER_VERTICAL)
            }
            6 -> paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)//左下角
            7 -> {
                paramsButtons.addRule(RelativeLayout.CENTER_HORIZONTAL)//下边正中
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
            8 -> {
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)//右下角
                paramsButtons.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            }
        }
        layoutButtons.layoutParams=paramsButtons//重绘
        buttonsRefresh()
    }

    private fun Int.toPxInt():Int=(this* Resources.getSystem().displayMetrics.density).toInt()
    private fun Float.toPxFloat():Float=(this* Resources.getSystem().displayMetrics.density).toFloat()

    fun enLarge(tmpx: Float,tmpy:Float,tmpz:Float,Fla:Int ){//布局放大
        val layout: RelativeLayout = this//按钮所在布局的放缩，布局上的按钮随之放缩
        var param1=0f
        if(abs(tmpx)<=7){
            param1=1.005f
        }else if(abs(tmpx)<=17) {
            param1=(((log10(abs(tmpx))/ log10(abs(tmpx )+6)) + 1) / 12 * 7).toFloat()

        }else if(abs(tmpx)<=40){
            param1=(((log(abs(tmpx), abs(tmpx )+ 23)) + 1) / 12 * 7).toFloat()
        }else if(abs(tmpx)<75){
            param1=(((log(abs(tmpx), abs(tmpx )+ 50)) + 1) / 12 * 7).toFloat()
        }else{
            param1=1.105f
        }
        param1=(param1+1)/2
        var param2=0f
        if(abs(tmpy)<=7){
            param2=1.005f
        }else if(abs(tmpy)<=17) {
            param2 = (((log10(abs(tmpx)) / log10(abs(tmpx) + 6)) + 1) / 12 * 7).toFloat()
        }
        param2=(param2+7)/8

        if(Fla==1) {

            layout.scaleX = (layout.scaleX) * 1.15F;//大小
            layout.scaleY = (layout.scaleY) * 1.15F;
        }else{
            if(tmpy==0f) {
                layout.scaleX = (layout.scaleX) * param1//*param2//大小
                layout.scaleY = (layout.scaleY) * param1//*param2
            }else{
                layout.scaleX = (layout.scaleX) * param1*param2//大小
                layout.scaleY = (layout.scaleY) * param1*param2
            }

        }
    }

    fun naRrow(tmpx: Float,tmpy:Float,tmpz:Float,Fla:Int){//布局缩小
        val layout: RelativeLayout = this//同上
        var param1=0f
        if(abs(tmpx)<=7){
            param1=1.005f
        }else if(abs(tmpx)<=17) {
            param1=(((log10(abs(tmpx))/ log10(abs(tmpx )+6)) + 1) / 12 * 7).toFloat()
        }else if(abs(tmpx)<=40){
            param1=(((log(abs(tmpx), abs(tmpx )+ 23)) + 1) / 12 * 7).toFloat()
        }else if(abs(tmpx)<75){
            param1=(((log(abs(tmpx), abs(tmpx )+ 50)) + 1) / 12 * 7).toFloat()
        }else{
            param1=1.105f
        }
        param1=(param1+1)/2
        var param2=0f
        if(abs(tmpy)<=7){
            param2=1.005f
        }else if(abs(tmpy)<=17) {
            param2 = (((log10(abs(tmpx)) / log10(abs(tmpx) + 6)) + 1) / 12 * 7).toFloat()
        }
        param2=(param2+7)/8
        if(Fla==1) {
            layout.scaleX = (layout.scaleX) * 0.85F;
            layout.scaleY = (layout.scaleY) * 0.85F;
        }else{
            if(tmpy==0f){
                layout.scaleX=(layout.scaleX)/param1//*param2
                layout.scaleY=(layout.scaleY)/param1//*param2
            }else{
                layout.scaleX=(layout.scaleX)/(param1*param2)
                layout.scaleY=(layout.scaleY)/(param1*param2)
            }

        }

    }

    fun horizentalShift(ang:Float){
        println("水平平移阶段")
        val layout: RelativeLayout = this
        if(ang>0) {
            layout.translationX = layout.translationX - 10//针对图像左边，单位pixel
        }else if(ang<0){
            layout.translationX = layout.translationX + 10
        }
    }

    fun verticalShift(ang:Float){
        println("垂直平移阶段")
        val layout: RelativeLayout = this
        if(ang<0) {
            layout.translationY = layout.translationY + 10//针对top(不知道是那个top)，单位pixel
        }else if(ang>0){
            layout.translationY = layout.translationY - 10
        }
    }
    //设置布局的位置
    fun setLocation(x: Float, y:Float) {

    }

    //设置布局的大小
    fun setSizeOfLayout() {

    }
    //12345依次为触点，触点上，触点下，触点外，中心点，偏移量为200px
    fun setZC(option:Int,a:Float,b:Float,suitableHand:Int){ //a是触摸点x，b是触摸点Y,最后一个参数,1是右手,2是左手,默认是右手
        val layout: RelativeLayout = findViewById(R.id.changeableLayout)
        val layoutX = layout.translationX
        val layoutY = layout.translationY
        var newX = a-layoutX
        var newY = b-layoutY-150

        when(option){
            1->{//触点中心
                layout.translationX = layout.translationX + (layout.pivotX - newX) * (1 - layout.scaleX)
                layout.translationY = layout.translationY + (layout.pivotY - newY) * (1 - layout.scaleY)
                layout.pivotX=newX
                layout.pivotY=newY
            }
            2->{//上
                newY -= 300
                layout.translationX = layout.translationX + (layout.pivotX - newX) * (1 - layout.scaleX)
                layout.translationY = layout.translationY + (layout.pivotY - newY) * (1 - layout.scaleY)
                layout.pivotX=newX
                layout.pivotY=newY
            }
            3->{//下
                newY += 300
                layout.translationX = layout.translationX + (layout.pivotX - newX) * (1 - layout.scaleX)
                layout.translationY = layout.translationY + (layout.pivotY - newY) * (1 - layout.scaleY)
                layout.pivotX=newX
                layout.pivotY=newY
            }
            4->{//外
                if(suitableHand==1)newX -= 300
                else newX += 300
                layout.translationX = layout.translationX + (layout.pivotX - newX) * (1 - layout.scaleX)
                layout.translationY = layout.translationY + (layout.pivotY - newY) * (1 - layout.scaleY)
                layout.pivotX=newX
                layout.pivotY=newY
            }
            5->{//中心
                val width = Resources.getSystem().displayMetrics.widthPixels
                val height = Resources.getSystem().displayMetrics.heightPixels
                val c = (width/2).toFloat()//屏幕中心点
                val d = (height/2).toFloat()
                newX = c-layoutX
                newY = d-layoutY

                layout.translationX = layout.translationX + (layout.pivotX - newX) * (1 - layout.scaleX)
                layout.translationY = layout.translationY + (layout.pivotY - newY) * (1 - layout.scaleY)
                layout.pivotX=newX
                layout.pivotY=newY
            }
        }
    }


    //释放音频资源
    fun release() {
        errorAudioPlayer.stop()
        errorAudioPlayer.release()
        rightAudioPlayer.stop()
        rightAudioPlayer.release()
    }
    fun iNit(){
        for(i in 0..15){
            Ti[i] = 0L
            Ts[i] = 0L
            To[i] = 0L
        }
        for(i in 0..2){
            timestamp[i]=0F
            angle[i]=0F
        }
        tmpt = 0L

        flag = 0
        lockdown = 0
    }
    fun initSaveDataMethod() {
        dataList = LinkedBlockingDeque()
        val excelPath = getExcelDir() + File.separator + "User_" + subjectInfo + "_" + 0 + ".xls"
        saveToExcel = SaveToExcel(excelPath)
        runnable = SaveDataRunnable(saveToExcel, dataList)
        Thread(runnable).start()
    }
    fun getExcelDir(): String {
        //SD卡指定文件夹
        val sdcardPath = Environment.getExternalStorageState().toString()
        val dir = File(sdcardPath + File.separator + "OneHand-Excel" + File.separator + "User_" + subjectInfo)
        if (dir.exists()) {
            return dir.toString()
        } else {
            dir.mkdirs();
            return dir.toString()
        }
    }

    fun taskCompleted(correct: Int) {
        when(correct) {
            0 ->  //添加一条记录到List
                try {
                    dataList.put(
                        ExperimentData("1",
                            0,
                            MainActivity.to.toDouble(),
                            MainActivity.zc.toDouble(),
                            MainActivity.cm.toDouble(),
                            To[select].toDouble(),
                            Ti[select].toDouble(),
                            Ts[select].toDouble(),
                            0.0
                        )
                    )
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            1 -> //添加一条记录到List
                try {
                    dataList.put(
                        ExperimentData("1",
                            0,
                            MainActivity.to.toDouble(),
                            MainActivity.zc.toDouble(),
                            MainActivity.cm.toDouble(),
                            (-1).toDouble(),
                            (-1).toDouble(),
                            (-1).toDouble(),
                            1.0
                        )
                    )
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            1 -> //添加一条记录到List
                try {
                    dataList.put(
                        ExperimentData("1",
                            0,
                            MainActivity.to.toDouble(),
                            MainActivity.zc.toDouble(),
                            MainActivity.cm.toDouble(),
                            (-1).toDouble(),
                            (-1).toDouble(),
                            (-1).toDouble(),
                            1.0
                        )
                    )
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            2 ->
                try {
                    dataList.put(
                        ExperimentData("1",
                            0,
                            MainActivity.to.toDouble(),
                            MainActivity.zc.toDouble(),
                            MainActivity.cm.toDouble(),
                            (-1).toDouble(),
                            (-1).toDouble(),
                            (-1).toDouble(),
                            2.0
                        )
                    )
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
        }
    }


}