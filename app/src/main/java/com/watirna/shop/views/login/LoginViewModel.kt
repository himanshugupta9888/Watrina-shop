package com.watirna.shop.views.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.BuildConfig
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.LoginModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.Constants
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.utils.WebApiConstants
import javax.inject.Inject

class  LoginViewModel:BaseViewModel<LoginNavigator>(){
    @Inject
    lateinit var  sessionManager: SessionManager

    @Inject
    lateinit var  shopRepository: ShopRepository

    init {
        AppController.appComponent.inject(this)
    }

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    var loginResponseData=MutableLiveData<LoginModel>()
    var loadingLiveData=MutableLiveData<Boolean>()


    fun validateInputs(){
       if(navigator.validate()){
           postLogin()
       }
    }

    fun postLogin(){
        loadingLiveData.value=true
         val params=HashMap<String,String>()
        params[WebApiConstants.LoginConstants.EMAIL]=email.value.toString()
        params[WebApiConstants.LoginConstants.PASSWORD]=password.value.toString()
        params[WebApiConstants.LoginConstants.DEVICETOKEN]=sessionManager.get(PreferenceKey.DEVICE_TOKEN)
        params[WebApiConstants.LoginConstants.DEVICETYPE]=Constants.DEVICETYPE
        params[WebApiConstants.AUTHKEY.SALT_KEY]=BuildConfig.SALT_KEY
        getCompositeDisposable().addAll(shopRepository.callLoginApi(params,object:ApiListener{
            override fun success(successData: Any) {
                loginResponseData.value=successData as LoginModel
                loadingLiveData.value=false
            }

            override fun fail(failData: Throwable) {
                if(failData.message.toString() == "HTTP 422 Unprocessable Entity")
                    navigator.showError("Given account is disabled by admin")
                else
                    navigator.showError(getErrorMessage(failData))
                loadingLiveData.value=false

                Log.e("LoginViewModel",failData.message.toString())

            }

        }))
    }

    fun forgetPasswordFun(){
        navigator.forgetPasswordFun()
    }
}