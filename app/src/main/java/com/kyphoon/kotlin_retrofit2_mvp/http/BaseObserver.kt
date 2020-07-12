package com.kyphoon.kotlin_retrofit2_mvp.http

import com.kyphoon.kotlin_retrofit2_mvp.base.BaseView
import io.reactivex.observers.DisposableObserver

/**
 *
 * @ProjectName: kotlin_retrofit2_mvp
 * @Package: com.kyphoon.kotlin_retrofit2_mvp.http
 * @ClassName: BaseObserver
 * @Description:http请求返回封装，作为弹窗显示，进度条显示的基础类
 * @Author: kyphoon
 * @CreateDate: 2020-07-12 16:12
 * @UpdateUser:
 * @UpdateDate: 2020-07-12 16:12
 * @UpdateRemark:
 * @Version: 1.0
 */
abstract class BaseObserver<T>(private val baseview : BaseView, private val show : Boolean) : DisposableObserver<T>() {

    override fun onStart() {
        if(show && baseview != null){
            baseview.showloaddialog()
        }
    }

    override fun onComplete() {
        if(show && baseview != null){
            baseview.hidloaddialog()
        }
    }

    override fun onNext(t: T) {
        Onsuccess(t)
    }

    override fun onError(e: Throwable) {
        if(show && baseview != null){
            if(e.toString().indexOf("ConnectException") != -1) {
                baseview.OnBaseError("“网络已断开，请重新连接网络后重试！")
            }else{
                baseview.OnBaseError(e.toString())
            }
        }
        Onerror(e.toString())
    }

    //访问成功之后调用
    abstract fun Onsuccess(t : T)

    //用于处理系统异常
    abstract fun Onerror(msg : String)
}