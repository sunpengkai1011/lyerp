package com.longyuan.lyerp.network

import android.text.TextUtils
import android.util.Log
import com.longyuan.lyerp.Constants
import com.longyuan.lyerp.GeneralUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils {

    fun <T> getService(header: Map<String, String>?, baseUrl: String, serviceClass: Class<T>): T{
        return getDefaultRetrofit(header, baseUrl).create(serviceClass)
    }

    fun <T> getService(baseUrl: String, serviceClass: Class<T>): T {
        return getDefaultRetrofit(null, baseUrl).create(serviceClass)
    }

    private fun getDefaultRetrofit(header: Map<String, String>?, baseUrl: String): Retrofit{
        var mBaseUrl = baseUrl
        val httpClientBuilder = getOkHttpClientBuilder(header)
        val client = httpClientBuilder.build()

        when{
            !mBaseUrl.contains("?") && mBaseUrl.lastIndexOf("/") != mBaseUrl.length - 1 -> mBaseUrl += "/"
            TextUtils.isEmpty(mBaseUrl) -> mBaseUrl = "http://localhost/"
        }

        return Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GeneralUtils.gson)).build()
    }

    private fun getOkHttpClientBuilder(header: Map<String, String>?): OkHttpClient.Builder{
        val httpClientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addNetworkInterceptor(loggingInterceptor)
        if (header != null && header.isNotEmpty()){
            httpClientBuilder.interceptors().add(Interceptor {
                Log.d(Constants.LOG_RETROFIT, "intercept called")
                val origin = it.request()

                val requestBuilder = origin.newBuilder()
                val iterator = header.keys.iterator()
                while (iterator.hasNext()){
                    val key = iterator.next()
                    val value = header[key]
                    requestBuilder.addHeader(key, value)
                }

                val request = requestBuilder.build()
                it.proceed(request)
            })
        }
        return httpClientBuilder
    }
}