package com.kyphoon.kotlin_retrofit2_mvp.http

import com.huati.intelligentparking.http.Okhttp3Factory
import com.kyphoon.kotlin_retrofit2_mvp.utils.PropertiesUtil
import com.kyphoon.kotlin_retrofit2_mvp.widget.JsonOrXmlConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 *
 * @ProjectName: kotlin_retrofit2_mvp
 * @Package: com.kyphoon.kotlin_retrofit2_mvp.http
 * @ClassName: HttpServiceGenerator
 * @Description: http请求类
 * @Author: kyphoon
 * @CreateDate: 2020-07-12 17:03
 * @UpdateUser:
 * @UpdateDate: 2020-07-12 17:03
 * @UpdateRemark:
 * @Version: 1.0
 */
object HttpServiceGenerator {

    private var sWebApi: WebApi = webApi()

    @Synchronized
    fun webApi(): WebApi {
        if (sWebApi == null) {
            val host = PropertiesUtil.getPropertiesMsg("httpConfig","host")
            val retrofitBuilder = Retrofit.Builder()
            retrofitBuilder.baseUrl(host)
            retrofitBuilder.client(Okhttp3Factory.sharedclient())
            retrofitBuilder.addConverterFactory(JsonOrXmlConverterFactory.create())
            retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            val retrofit = retrofitBuilder.build()
            sWebApi = retrofit.create(WebApi::class.java)
        }
        return sWebApi
    }
}