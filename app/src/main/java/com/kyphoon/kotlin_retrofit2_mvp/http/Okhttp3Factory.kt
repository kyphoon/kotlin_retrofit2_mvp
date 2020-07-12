package com.huati.intelligentparking.http

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.cloud.apigateway.sdk.utils.Client
import com.cloud.apigateway.sdk.utils.Request
import com.huati.intelligentparking.utils.DaoUtil
import com.huati.intelligentparking.utils.PropertiesUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream
import java.lang.ref.WeakReference
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 *
 * @ProjectName: IntelligentParking_huati
 * @Package: com.huati.intelligentparking.http
 * @ClassName: Okhttp3Factory
 * @Description: okhttp3请求封装类
 * @Author: kyphoon
 * @CreateDate: 2020-04-29 14:17
 * @UpdateUser:
 * @UpdateDate: 2020-04-29 14:17
 * @UpdateRemark:
 * @Version: 1.0
 */
object Okhttp3Factory {

    var weakHashMap : WeakReference<OkHttpClient> = WeakReference<OkHttpClient>(null)!!

    @Synchronized
    fun sharedclient() : OkHttpClient? {
        var client : OkHttpClient? = weakHashMap?.get()
        if(client == null){
            client = OkHttpClient.Builder()
                .sslSocketFactory(getSSLFactory())
                .hostnameVerifier(object : HostnameVerifier{
                    override fun verify(hostname: String?, session: SSLSession?): Boolean {
                        return true
                    }
                })
//                .sslSocketFactory(getSSLSocketFactory(MyApplication.getContext(), arrayListOf(R.raw.ic_actionbar_call)))
                .addInterceptor(ReadCookIntercepter())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .build()
            weakHashMap = WeakReference<OkHttpClient>(client)
        }
        return client
    }

    //将证书添加到工程中
    //自定义信任管理器TrustManager
    //用自定义TrustManager代替系统默认的信任管理器
    fun getSSLSocketFactory(context : Context,certificates : ArrayList<Int>) : SSLSocketFactory{
        //生成证书
        val certificateFactory : CertificateFactory
        certificateFactory = CertificateFactory.getInstance("X.509")
        //Create a KeyStore containing our trusted CAs
        var  keystore : KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keystore.load(null, null)

        for (i in certificates) {
            //读取本地证书
            var input : InputStream = context.getResources().openRawResource(certificates[i])
            keystore.setCertificateEntry(i.toString(), certificateFactory.generateCertificate(input))

            if (input != null) {
                input.close()
            }
        }

        //Create a TrustManager that trusts the CAs in our keyStore
        var trustManagerFactory : TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keystore)

        //Create an SSLContext that uses our TrustManager
        var sslContext : SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagerFactory.getTrustManagers(),SecureRandom())
        return sslContext.getSocketFactory()
    }

    //不进行验证
    private fun getSSLFactory(): SSLSocketFactory {
        //证书忽略添加下面代码（1）打开即可
//         Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf<X509Certificate>()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        return sslContext.socketFactory
    }

    //添加请求头信息,和公共请求部分
    class ReadCookIntercepter : Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            //添加公共请求部分sign
            var newurl = original.url().toString() + "&sign="+DaoUtil.getUserInfo(1)?.sign
            val builder = original.url()
                .newBuilder()
                .scheme(original.url().scheme())
                .host(original.url().host())
            var Authorization = DaoUtil.getUserInfo(1)?.accessToken
            val requestBuilder = original.newBuilder()
            if(original.url().toString().endsWith("platLogin") || original.url().toString().endsWith("platLogout")){
                requestBuilder.method(original.method(), original.body())
                    .url(builder.build())
            }else if(original.url().toString().endsWith("uploadimg")){
                requestBuilder.method(original.method(), original.body())
                    .url("http://"+DaoUtil.getUserInfo(1)!!.zimgUrl+":"+DaoUtil.getUserInfo(1)!!.proxyPort+"/upload")
            }else if(original.url().toString().endsWith("requestGetNewVersion")){
                requestBuilder.method(original.method(), original.body())
                    .url(original.url().toString() + "?sign="+DaoUtil.getUserInfo(1)?.sign)
            }else if(original.url().toString().indexOf("HWYOcrrequest") != -1){
                var platerequest : Request = Request()
                platerequest.appKey = PropertiesUtil.getPropertiesMsg("HWYOcrConfig","AK")
                platerequest.appSecrect = PropertiesUtil.getPropertiesMsg("HWYOcrConfig","Sk")
                platerequest.setMethod(original.method())
                platerequest.url = "https://"+PropertiesUtil.getPropertiesMsg("HWYOcrConfig","Region")+"/v1.0/ocr/license-plate"
                platerequest.addHeader("Content-Type", "application/json")
                platerequest.body = original.url().query()!!.split("=")[1]
                return chain.proceed(Client.signOkhttp(platerequest))
            }else{
                requestBuilder.method(original.method(), original.body())
                    .url(newurl)
            }

            // requestBuilder.addHeader("Content-Type","application/json");
            Log.v(
                "OkHttp",
                "Url: " + original.url().toString()
            ) // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            if(original.url().toString().endsWith("requestImageRecognition")){

            }else{
                if (!TextUtils.isEmpty(Authorization)) {
                    //添加cookie
                    requestBuilder.addHeader("tokenCookie", Authorization)
                    Log.v(
                        "OkHttp",
                        "Adding Header: $Authorization"
                    ) // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
                }else{
                    requestBuilder.addHeader("tokenCookie", "")
                    Log.v(
                        "OkHttp",
                        "Adding Header: null"
                    )
                }
            }
            val request = requestBuilder.build()
            return chain.proceed(request)
        }

    }
}