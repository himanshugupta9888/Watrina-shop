package com.watirna.shop.utils

import android.content.SharedPreferences
import com.watirna.shop.application.AppController
import javax.inject.Inject

class SessionManager {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    init {
        AppController.appComponent.inject(this)
    }


    inline fun <reified T> put(key: String, value: T) {
        val editor = sharedPreferences.edit()
        when (T::class) {
            Boolean::class -> editor.putBoolean(key, value as Boolean)
            Float::class -> editor.putFloat(key, value as Float)
            Int::class -> editor.putInt(key, value as Int)
            Long::class -> editor.putLong(key, value as Long)
            String::class -> editor.putString(key, value as String)
            Set::class -> editor.putStringSet(key, value as Set<String>)
            else -> throw UnsupportedOperationException(Constants.ThrowableErrorMsg.UNSUPPORTEDTYPE)
        }
        editor.apply()
    }

    inline fun <reified T> get(key: String): T {
        return when (T::class) {
            Boolean::class -> sharedPreferences.getBoolean(key, false) as T
            Float::class -> sharedPreferences.getFloat(key, -1f) as T
            Int::class -> sharedPreferences.getInt(key, -1) as T
            Long::class -> sharedPreferences.getLong(key, -1L) as T
            String::class -> sharedPreferences.getString(key, "") as T
            Set::class -> sharedPreferences.getStringSet(key, null) as T
            else -> throw UnsupportedOperationException(Constants.ThrowableErrorMsg.UNSUPPORTEDTYPE)
        }
    }

    inline fun <reified T> get(key: String, defValue: T? = null): T? {
        return when (T::class) {
            Boolean::class -> sharedPreferences.getBoolean(key, defValue as? Boolean ?: false) as T?
            Float::class -> sharedPreferences.getFloat(key, defValue as? Float ?: -1f) as T?
            Int::class -> sharedPreferences.getInt(key, defValue as? Int ?: -1) as T?
            Long::class -> sharedPreferences.getLong(key, defValue as? Long ?: -1L) as T?
            String::class -> sharedPreferences.getString(key, defValue as? String) as T?
            Set::class -> sharedPreferences.getStringSet(key, defValue as? Set<String>) as T?
            else -> throw UnsupportedOperationException(Constants.ThrowableErrorMsg.UNSUPPORTEDTYPE)
        }
    }

    fun removeAll() {
        sharedPreferences.edit().clear().apply()
    }

    fun clearAuthToken() {
        sharedPreferences.edit().remove(PreferenceKey.ACCESS_TOKEN)
    }
}