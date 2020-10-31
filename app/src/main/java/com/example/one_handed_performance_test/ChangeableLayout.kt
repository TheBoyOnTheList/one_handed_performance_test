package com.example.one_handed_performance_test

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.changeable_layout.view.*

class ChangeableLayout(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.changeable_layout, this)
    }
    fun isClickTheTarget(): Boolean {
        return buttons.getClickTheTarget()
    }
    fun refresh() {
        this.translationX = 10f
        this.translationY = 10f
        val layoutButtons: LinearLayout = findViewById(R.id.buttons)//重新设置按钮的相对位置
        val paramsButtons: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(50.toPx(),50.toPx())
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
        buttons.refresh()
        layoutButtons.layoutParams=paramsButtons//重绘
    }
    private fun Int.toPx():Int=(this* Resources.getSystem().displayMetrics.density).toInt()
}