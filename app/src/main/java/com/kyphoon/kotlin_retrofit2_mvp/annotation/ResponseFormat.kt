package com.kyphoon.kotlin_retrofit2_mvp.annotation

/**
 *
 * @ProjectName: kotlin_retrofit2_mvp
 * @Package: com.kyphoon.kotlin_retrofit2_mvp.annotation
 * @ClassName: ResponseFormat
 * @Description:网络请求返回值解析方法注解
 * @Author: kyphoon
 * @CreateDate: 2020-07-12 16:21
 * @UpdateUser:
 * @UpdateDate: 2020-07-12 16:21
 * @UpdateRemark:
 * @Version: 1.0
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ResponseFormat(
    //value取值范围（string，gson，xml）
    val value : String
)