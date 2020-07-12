package com.kyphoon.kotlin_retrofit2_mvp.base

import android.app.Dialog

/**
 *
 * @ProjectName: kotlin_retrofit2_mvp
 * @Package: com.kyphoon.kotlin_retrofit2_mvp.base
 * @ClassName: BaseView
 * @Description:ui界面基础操作定义
 * @Author: kyphoon
 * @CreateDate: 2020-07-12 16:15
 * @UpdateUser:
 * @UpdateDate: 2020-07-12 16:15
 * @UpdateRemark:
 * @Version: 1.0
 */
interface BaseView {

    fun showloaddialog(msg : String = "加载中...") : Dialog

    fun hidloaddialog()

    fun OnBaseError(msg : String)

    fun onBaseWarn(msg : String)
}