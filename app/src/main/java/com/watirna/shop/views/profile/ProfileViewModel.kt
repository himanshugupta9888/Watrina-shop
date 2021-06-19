package com.watirna.shop.views.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.ProfileModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.*
import javax.inject.Inject

class ProfileViewModel : BaseViewModel<ProfileNavigator>() {

    @Inject
    lateinit var shopRepository: ShopRepository

    @Inject
    lateinit var commanMethods: CommanMethods

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }


    val profileResponse = MutableLiveData<ProfileModel>()
    val loadingLiveData = MutableLiveData<Boolean>()


    var userName = MutableLiveData<String>("")
    var phoneNumber = MutableLiveData<String>("")
    var email = MutableLiveData<String>("")
    var city = MutableLiveData<String>("")
    var country = MutableLiveData<String>("")
    var country_code = MutableLiveData<String>("")
    var profileImage =MutableLiveData<String>("")


    fun validateInputs() {
        if (navigator.validateProfileInputs()) {

            postProfile()

        }
    }

    private fun postProfile() {

        loadingLiveData.value = true
        var id: Int
        id = sessionManager.sharedPreferences.getInt(PreferenceKey.SHOP_ID, Int.MAX_VALUE)

        val params = HashMap<String, String>()
        params[WebApiConstants.Profile.USERNAME] = userName.value.toString()
        params[WebApiConstants.Profile.COUNTRYCODE] = country_code.value.toString()
        params[WebApiConstants.Profile.CITY] = city.value.toString()
        params[WebApiConstants.Profile.EMAIL] = email.value.toString()
        params[WebApiConstants.Profile.PHONENUMBER] = phoneNumber.value.toString()
        getCompositeDisposable().addAll(shopRepository.getProfile(params, id, object : ApiListener {
            override fun success(successData: Any) {
                profileResponse.postValue(successData as ProfileModel)
                loadingLiveData.value = false
                Log.d("ProfileViewModel", successData.message.toString())
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
                loadingLiveData.value = false
                Log.e("ProfileViewModel", failData.message.toString())

            }
        }))
    }


    fun gotoCountryPicker() {
        navigator.openCountryPicker()
    }

    fun selectImage(): Unit {
        navigator.selectProfileImage()

    }


    fun profile() {

        loadingLiveData.value = true
        val params = HashMap<String, String>()
        var id: Int

        id = sessionManager.sharedPreferences.getInt(PreferenceKey.SHOP_ID, Int.MAX_VALUE)

        getCompositeDisposable().addAll(shopRepository.getProfile(params, id, object : ApiListener {
            override fun success(successData: Any) {

                profileResponse.value = successData as ProfileModel
                loadingLiveData.value = false
            }

            override fun fail(failData: Throwable) {

                commanMethods.getErrorMessage(failData)
                loadingLiveData.value = false
            }
        }))
    }

}