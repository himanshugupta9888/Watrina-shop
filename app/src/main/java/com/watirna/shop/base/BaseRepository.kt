package com.watirna.shop.base

import com.watirna.shop.BuildConfig
import com.watirna.shop.application.AppController
import com.watirna.shop.utils.NetworkError
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

open class BaseRepository {

    @Inject
    lateinit var retrofit: Retrofit

    init {
        AppController.appComponent.inject(this)
    }

    fun <T> createApiClient(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun <T> createApiClient(baseUrl: String, service: Class<T>): T {
        return reconstructedRetrofit(baseUrl).create(service)
    }

    @Singleton
    fun reconstructedRetrofit(baseUrl: String): Retrofit {
        return retrofit.newBuilder()
            .baseUrl("$baseUrl/")
            .build()
    }

    fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is JsonSyntaxException -> {
                if (BuildConfig.DEBUG) e.message.toString()
                else NetworkError.DATA_EXCEPTION
            }
            is HttpException -> { if(e.code()==401) getErrorMessage(e.response()!!.errorBody()!!) else ""}
            is SocketTimeoutException -> NetworkError.TIME_OUT
            is IOException -> NetworkError.IO_EXCEPTION
            else -> NetworkError.SERVER_EXCEPTION
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