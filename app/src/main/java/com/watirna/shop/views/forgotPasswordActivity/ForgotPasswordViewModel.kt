package com.watirna.shop.views.forgotPasswordActivity

import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class ForgotPasswordViewModel : BaseViewModel<ForgotPasswordNavigator>() {

    @Inject
    lateinit var shopRepository: ShopRepository

    init {
        AppController.appComponent.inject(this)
    }

    var email = MutableLiveData<String>()
    var phone = MutableLiveData<String>()
    lateinit var country_code: String
    var accountype: String = "email"
    var username: String = ""
    var otp: Int = 0
    var errorResponse = MutableLiveData<String>()
    var loadingProgress = MutableLiveData<Boolean>()
    var forgotPasswordResponse = MutableLiveData<ForgotPasswordResponse>()
    var resetResponse = MutableLiveData<ResetPasswordResponse>()
    val VALID_EMAIL_ADDRESS_REGEX: Pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    fun verifyEmailApicall() {
        loadingProgress.value = true
        if (email.value!!.toString() !=null && email.value!!.toString() !="") {
            if(!validate(email.value!!.toString())){
                loadingProgress.value = false
                navigator.showError("Please enter valid email id")
            } else {
                val hashMap = HashMap<String, String>()
                hashMap["salt_key"] = "MQ=="
                hashMap["email"] = email.value!!
//            getCompositeDisposable().add(shopRepository
//                .forgotPassword(this,hashMap))
                getCompositeDisposable().addAll(
                    shopRepository.forgotPassword(hashMap,
                        object : ApiListener {
                            override fun success(successData: Any) {
                                loadingProgress.value = false
                                forgotPasswordResponse.postValue(successData as ForgotPasswordResponse)
                            }

                            override fun fail(failData: Throwable) {
                                loadingProgress.value = false
                                navigator.showError("Email Id not exit")
                                println("forgot pwd error " + failData.message)
                            }
                        }))
            }
        } else {
            navigator.showError("Please check the email Id")
        }
    }

    fun resetPasswordApicall(otp: String, password: String) {
            loadingProgress.value = true
            val hashMap = HashMap<String, String>()
            hashMap["username"] = username
            hashMap["otp"] = otp
            hashMap["password"] = password

            getCompositeDisposable().addAll(shopRepository.resetPassword(hashMap,
                object : ApiListener {
                    override fun success(successData: Any) {
                        loadingProgress.value = false
                        resetResponse.postValue(successData as ResetPasswordResponse)
                    }

                    override fun fail(failData: Throwable) {
                        loadingProgress.value = false
                        navigator.showError("Something went wrong!")
                    }
                }))
        }



    fun validate(emailStr: String?): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
        return matcher.find()
    }

}