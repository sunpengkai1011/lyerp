package com.longyuan.lyerp.network

import android.app.AlertDialog
import android.content.Context
import com.longyuan.lyerp.GeneralUtils
import com.longyuan.lyerp.network.exception.DataFormatException
import com.longyuan.lyerp.network.exception.EmptyDataException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class BaseRequest<T, R, M>(private val context: Context) {

    private val loadingDialog = GeneralUtils.getWaitDialog(context)

    protected abstract fun getHeader(): Map<String, String>?

    protected abstract fun getUrl(): String

    protected abstract fun getEndpointClass(): Class<T>

    protected abstract fun getEndpoint(endpoint: T): Single<R>

    protected abstract fun dealWithResponse(response: R): M

    fun getEndpoint(): T{
        return RetrofitUtils.getService(getHeader(), getUrl(), getEndpointClass())
    }

    fun getData(): Single<M> {
        loadingDialog.show()
        val endpoint = getEndpoint()
        return getEndpoint(endpoint).flatMap {
            dealSuccess(it)
        }.onErrorResumeNext {
            dealError(it)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun dealSuccess(response: R): Single<M>{
        loadingDialog.dismiss()
        return Single.create {
            when{
                response != null -> {
                    val model = dealWithResponse(response)
                    when{
                        model != null -> {
                            Timber.d("getData success return")
                            if (model is List<*>){
                                val modelList = model.toList()
                                if (modelList.isEmpty()){
                                    Timber.d("getData error: list empty")
                                    it.onError(EmptyDataException())
                                    return@create
                                }
                            }
                            it.onSuccess(model)
                        }
                        else -> {
                            Timber.d("getData parse error")
                            it.onError(DataFormatException())
                        }
                    }
                }
                else -> {
                    Timber.d("getData error")
                    it.onError(EmptyDataException())
                }
            }
        }
    }

    private fun dealError(throwable: Throwable): Single<M>{
        loadingDialog.dismiss()
        return Single.error(throwable)
    }
}