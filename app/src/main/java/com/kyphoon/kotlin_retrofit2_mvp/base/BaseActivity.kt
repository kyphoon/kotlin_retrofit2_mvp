package com.kyphoon.kotlin_retrofit2_mvp.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 *
 * @ProjectName: kotlin_retrofit2_mvp
 * @Package: com.kyphoon.kotlin_retrofit2_mvp.base
 * @ClassName: BaseActivity
 * @Description:
 * @Author: kyphoon
 * @CreateDate: 2020-07-12 16:03
 * @UpdateUser:
 * @UpdateDate: 2020-07-12 16:03
 * @UpdateRemark:
 * @Version: 1.0
 */
abstract class BaseActivity <T : BasePresenter> : AppCompatActivity(),BaseView, View.OnClickListener {

    protected lateinit var presenter : T

    protected abstract fun createPresenter() : T

    protected abstract fun getLayoutId() : Int

    protected abstract fun  initView()

    protected abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LayoutInflater.from(this).inflate(getLayoutId(), null))

        presenter = createPresenter()

        initView()
        initData()
    }

    override fun onClick(v: View?) {

    }


    override fun onDestroy() {
        super.onDestroy()
        //销毁网络请求
        presenter?.detachView()
    }

    override fun showloaddialog(msg: String): Dialog {
        return Dialog(this)
    }

    override fun hidloaddialog() {

    }

    override fun onBaseWarn(msg: String) {

    }

    override fun OnBaseError(msg: String) {

    }

}