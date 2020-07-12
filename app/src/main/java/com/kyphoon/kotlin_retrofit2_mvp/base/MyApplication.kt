package com.kyphoon.kotlin_retrofit2_mvp.base

import android.app.Application

/**
 *
 * @ProjectName: kotlin_retrofit2_mvp
 * @Package: com.kyphoon.kotlin_retrofit2_mvp.base
 * @ClassName: MyApplication
 * @Description:自定义application类，用于初始化第三方sdk及全局处理
 * @Author: kyphoon
 * @CreateDate: 2020-07-12 16:03
 * @UpdateUser:
 * @UpdateDate: 2020-07-12 16:03
 * @UpdateRemark:
 * @Version: 1.0
 */
class MyApplication : Application() {

    companion object{
        var  mContext:Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

}