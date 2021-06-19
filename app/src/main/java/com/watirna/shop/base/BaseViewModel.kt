package com.watirna.shop.base

import androidx.lifecycle.ViewModel
import com.watirna.shop.BuildConfig
import com.watirna.shop.utils.NetworkError
import com.google.gson.JsonSyntaxException
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException

open class BaseViewModel<N>:ViewModel(){

    private var compositeDisposable = CompositeDisposable()
    private lateinit var mNavigator: WeakReference<N>
    var navigator: N
        get() = mNavigator.get()!!
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    fun getCompositeDisposable() = compositeDisposable

      override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is JsonSyntaxException -> {
                if (BuildConfig.DEBUG) e.message.toString()
                else NetworkError.DATA_EXCEPTION
            }
            is HttpException -> { if(e.code()==401) getErrorMessage(e.response()!!.errorBody()!!) else e.response()?.message().toString()}
            is SocketTimeoutException -> NetworkError.TIME_OUT
            is IOException -> NetworkError.IO_EXCEPTION
            else -> NetworkError.SERVER_EXCEPTION
        }
    }


//    fun handleThrowable(t: Throwable): String {
//        when (t) {
//            is UnknownHostException, is ConnectException -> {
//                showErrorToast(mContext, mContext.getString(R.string.network_issue))
//            }
//            is ParseException, is JsonSyntaxException -> {
//                showErrorToast(mContext, mContext.getString(R.string.parser_exception))
//            }
//            is HttpException -> {
//                when (t.code()) {
//                    Constants.UN_AUTHORIZED -> {
//                        showErrorToast(mContext, mContext.getString(R.string.un_authorised_user))
//                        SessionUtils.clearSession()
//                    }
//                    Constants.UN_PROCESSABLE_ENTITY -> {
//                        showErrorToast(mContext, mContext.getString(R.string.un_processable_entity))
//                    }
//                    Constants.INTERNAL_SERVER_ERROR -> {
//                        showErrorToast(mContext, mContext.getString(R.string.internal_server_exception))
//                    }
//                }
//            }
//            else -> {
//                showErrorToast(mContext, mContext.getString(R.string.un_caught_exception))
//            }
//        }
//    }

    fun getStatusCode(e:Throwable):Int{
              return  when(e){
                       is HttpException -> (e.code())
                       else -> -1
                  }
           }

    private fun getErrorMessage(responseBody: ResponseBody): String {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message!!
        }
    }

}