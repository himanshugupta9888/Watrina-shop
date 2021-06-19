package com.fetch39.delivery.dependencies.interceptor

import android.util.Log
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException



class AuthTokenInterceptor(private val sessionManager: SessionManager) : Interceptor {
    private lateinit var requestBuilder: Request.Builder

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val original = chain.request()
            print("Auth -- "+"Bearer " + sessionManager.get<String>(PreferenceKey.ACCESS_TOKEN))
            requestBuilder = if (sessionManager.get<String>(PreferenceKey.ACCESS_TOKEN)!= "") {
                // Request customization: add request headers
                original.newBuilder()
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("Authorization", "Bearer " + sessionManager.get<String>(PreferenceKey.ACCESS_TOKEN))
                    .header("Connection", "close")
                    .method(original.method, original.body)
            } else {
                // Request customization: add request headers
                original.newBuilder()
                    .method(original.method, original.body)
            }
        } catch (e: Exception) {
            Log.e("Error", "Received an exception", e)
        }


        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}