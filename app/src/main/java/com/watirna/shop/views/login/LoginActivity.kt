package com.watirna.shop.views.login

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityLoginBinding
import com.watirna.shop.datamodel.LoginModel
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.dashboard.DashboardActivity
import com.watirna.shop.views.forgotPasswordActivity.ForgotPasswordActivity
import java.lang.Exception
import javax.inject.Inject

class  LoginActivity : BaseActivity<ActivityLoginBinding>(),LoginNavigator{
    @Inject
    lateinit var  commanMethods: CommanMethods

    @Inject
    lateinit var  sessionManager: SessionManager

    @Inject
    lateinit var  context: Context

    private  lateinit var  activityLoginBinding:ActivityLoginBinding
    private  lateinit var  loginViewModel: LoginViewModel


    init {
        AppController.appComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityLoginBinding=mViewDataBinding as ActivityLoginBinding
        loginViewModel=ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel.navigator=this
        activityLoginBinding.setLifecycleOwner(this)
        activityLoginBinding.loginViewModel=loginViewModel
        getObserverValues()

        activityLoginBinding.btnForgotPassword.setOnClickListener {
            forgetPasswordFun()
        }

    }

    override fun showError(errorMsg: String) {
        commanMethods.showToast(context,errorMsg,false)
    }

    override fun validate():Boolean {
        if (loginViewModel.email.value.isNullOrBlank()) {
            val message = resources.getString(R.string.email_empty)
            commanMethods.showToast(context,message,false)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(loginViewModel.email.value!!.trim())
                .matches()) {
            val message = resources.getString(R.string.email_invalid)
            commanMethods.showToast(context,message,false)
            return false
        }else if(loginViewModel.password.value.isNullOrBlank()) {
            val message = resources.getString(R.string.password_empty)
            commanMethods.showToast(context,message,false)
            return false
        }

        return  true
    }

    override fun forgetPasswordFun() {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    fun getObserverValues(){

        loginViewModel.loadingLiveData.observe(this, Observer<Boolean> {
            if(it){
                 baseLiveDataLoading.value=true
        }else{
                baseLiveDataLoading.value=false
        }
        })

        loginViewModel.loginResponseData.observe(this, Observer<LoginModel>{
            try{
            if(it!=null && it.statusCode.equals("200")){
                if (it.responseData!!.access_token!=null) {
                    sessionManager.put(PreferenceKey.ACCESS_TOKEN, it.responseData!!.access_token)
                }

                if (it.responseData!!.user!!.id!=null) {
                    sessionManager.put(PreferenceKey.SHOP_ID, it.responseData!!.user!!.id)
                }
                if(it.responseData!!.user!!.currency!=null) {
                    sessionManager.put(PreferenceKey.CURRENCY, it.responseData!!.user!!.currency)
                }
                if(it.responseData!!.user!!.currency_symbol!=null) {
                    sessionManager.put(PreferenceKey.CURRENCY_SYMBOL, it.responseData!!.user!!.currency_symbol)
                }

                //Store type set
                sessionManager.put(PreferenceKey.SHOP_TYPE, it.responseData!!.user?.store_type_id)
                sessionManager.put(PreferenceKey.SHOP_TYPE_NAME, it.responseData!!.user?.store_type)

                val dashBoardIntent=Intent(this,DashboardActivity::class.java)
                startActivity(dashBoardIntent)
                finish()
            }

            }
            catch(e:Exception){
                Log.e("Exception",e.toString())
            }

        })
    }

}