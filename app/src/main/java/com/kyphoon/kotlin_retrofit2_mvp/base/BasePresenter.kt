package com.kyphoon.kotlin_retrofit2_mvp.base

import com.kyphoon.kotlin_retrofit2_mvp.http.BaseObserver
import com.kyphoon.kotlin_retrofit2_mvp.http.HttpServiceGenerator
import com.kyphoon.kotlin_retrofit2_mvp.http.WebApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *
 * @ProjectName: kotlin_retrofit2_mvp
 * @Package: com.kyphoon.kotlin_retrofit2_mvp.base
 * @ClassName: BasePresenter
 * @Description:
 * @Author: kyphoon
 * @CreateDate: 2020-07-12 16:10
 * @UpdateUser:
 * @UpdateDate: 2020-07-12 16:10
 * @UpdateRemark:
 * @Version: 1.0
 */
class BasePresenter {

    //管理订阅事件disposable，然后在acivity销毁的时候，调用compositeDisposable.dispose()就可以切断所有订阅事件，防止内存泄漏。
    var compositeDisposable : CompositeDisposable? = null

    //提供给子类调用
    protected var webApi : WebApi = HttpServiceGenerator.webApi()

    //注销所有订阅事件
    fun detachView(){
        removeDisposable()
    }

    //添加管理订阅事件
    fun <T>addDisposable(observable : Observable<T>, observer : BaseObserver<T>) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer))
    }

    fun removeDisposable() {
        compositeDisposable?.dispose()
    }
}