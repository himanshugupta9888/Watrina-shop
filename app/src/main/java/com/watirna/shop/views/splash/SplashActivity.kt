package com.watirna.shop.views.splash

import android.content.Intent
import android.os.Handler
import androidx.databinding.ViewDataBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivitySplashBinding
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.dashboard.DashboardActivity
import com.watirna.shop.views.login.LoginActivity
import javax.inject.Inject

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    @Inject
    lateinit var sessionManager: SessionManager
    init {
        AppController.appComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        val handler = Handler()
        handler.postDelayed({ callDashBoardActivity() }, 3000)
        getDeviceToken()
    }

    fun callDashBoardActivity() {
        val token = sessionManager.get<String>(PreferenceKey.ACCESS_TOKEN)
        if (!token.isNullOrEmpty()) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    //  Log.w("Tag", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                println("RRR :: token = ${task.result?.token}")
                sessionManager.put(PreferenceKey.DEVICE_TOKEN, task.result?.token)
            })
    }

}