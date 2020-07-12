package com.kyphoon.kotlin_retrofit2_mvp.utils

import com.kyphoon.kotlin_retrofit2_mvp.base.MyApplication
import java.util.*

/**
 *
 * @ProjectName: kotlin_retrofit2_mvp
 * @Package: com.kyphoon.kotlin_retrofit2_mvp.utils
 * @ClassName: PropertiesUtil
 * @Description: 配置获取工具类
 * @Author: kyphoon
 * @CreateDate: 2020-07-12 15:16
 * @UpdateUser:
 * @UpdateDate: 2020-07-12 15:16
 * @UpdateRemark:
 * @Version: 1.0
 */
object PropertiesUtil{

    val properties : Properties = Properties()

    fun getPropertiesMsg(file : String,key : String) : String{
        properties.load(MyApplication.mContext?.assets?.open(file))
        return properties.getProperty(key)
    }
}