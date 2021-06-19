package com.watirna.shop.application

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.watirna.shop.dependencies.component.AppComponent
import com.watirna.shop.dependencies.component.DaggerAppComponent
import com.watirna.shop.dependencies.modules.ApplicationModule
import com.watirna.shop.dependencies.modules.NetworkModule
import com.watirna.shop.socket.SocketListener
import com.watirna.shop.socket.SocketManager
import com.watirna.shop.utils.Constants
import com.watirna.shop.utils.PreferencesHelper
import io.socket.emitter.Emitter

class AppController : Application() {

    companion object {
        lateinit var appComponent: AppComponent

    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        PreferencesHelper.setCustomPreferences(this, Constants.APP_NAME)
        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule(Constants.baseUrl))
            .applicationModule(ApplicationModule(this))
            .build()
        Stetho.initializeWithDefaults(this)
        configureSocket()
    }

    fun configureSocket() {
        SocketManager.setOnConnectionListener(object : SocketListener.CallBack {
            override fun onConnected() {
                SocketManager.onEvent(Constants.RoomName.NEW_REQ, Emitter.Listener {
                    Log.e("SOCKET", "SOCKET_SK status " + it[0])
                })
            }

            override fun onDisconnected() {
                Log.e("SOCKET", "SOCKET_SK disconnected")
            }

            override fun onConnectionError() {
                Log.e("SOCKET", "SOCKET_SK connection error")
            }

            override fun onConnectionTimeOut() {
                Log.e("SOCKET", "SOCKET_SK connection timeout")
            }
        })
        SocketManager.connect(Constants.baseUrl)
    }
}