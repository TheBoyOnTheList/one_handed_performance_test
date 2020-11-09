package com.example.one_handed_performance_test

/*
    实验数据存储类
    包括
    自变量:实验者名称user 倾斜角改变方向TO 缩放中心ZC 倾斜角对缩放的控制方式CM
    因变量:视图缩小阶段的时间Tc 视图放大阶段的时间记录为两部分T+Ts 出错次数
    kotlin自动实现getter
 */
class ExperimentData(val subjectID: Double, val feedback: Int, val block: Double, val TO: Double, val ZC: Double, val CM: Double,
                     val Tc: Double, val T: Double, val Ts: Double, val error: Double)