package com.watirna.shop.views.editrestaurant

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.CountryListResponse
import com.watirna.shop.datamodel.EditRestaurantModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.*
import javax.inject.Inject
import com.watirna.shop.BuildConfig.SALT_KEY as SALT_KEY1

class EditRestaurantViewModel : BaseViewModel<EditRestaurantNavigator>() {

    @Inject
    lateinit var shopRepository: ShopRepository

    @Inject
    lateinit var commanMethods: CommanMethods

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }

    var commonSuccessResponse = MutableLiveData<CommonSuccessResponse>()

    val editRestaurantResponse = MutableLiveData<EditRestaurantModel>()
    val loadingLiveData = MutableLiveData<Boolean>()


    var shopName = MutableLiveData("")
    var phoneNumber = MutableLiveData<String>("")
    var email = MutableLiveData<String>("")
    var city = MutableLiveData<String>("")
    var country = MutableLiveData<String>("India")
    var country_code = MutableLiveData<String>("")
    var cuisine = MutableLiveData<String>("")
    var contactPerson = MutableLiveData<String>("")
    var storePackageCharge = MutableLiveData<String>("")
    var status = MutableLiveData<String>("")
    var gst = MutableLiveData<String>("")
    var minimumAmount = MutableLiveData<String>("")
    var offer = MutableLiveData<String>("")
    var commission = MutableLiveData<String>("")
    var maximumDeliveryTime = MutableLiveData<String>("")
    var stroeResponseTime = MutableLiveData<String>("")
    var description = MutableLiveData<String>("")
    var landMark = MutableLiveData<String>("")
    var flatno = MutableLiveData<String>("")
    var street = MutableLiveData<String>("")
    var zone = MutableLiveData<String>("")
    var zipCode = MutableLiveData<String>("")
    var latitude = MutableLiveData<String>("")
    var longitude = MutableLiveData<String>()
    var selectedStatus ="1"
    var country_id: String = ""
    var city_id: String = ""
    var zoneid: String = ""
    var cuisineId: String = ""
    var countryListResponse = MutableLiveData<CountryListResponse>()
    var cuisineName = ""
    var strAddress:String=""
    var cuisineIds = ArrayList<Int>()
    var is_veg = MutableLiveData<String>("Pure Veg")
    var textWatch = true

    init {
      //  getCountryList()
    }

    fun validateInputs() {
        if (navigator.validateInputs()) {
            postValue()
        }
    }


    fun gotoCountryPicker() {
        navigator.openCountryPicker()
    }


    fun editRestaurant() {

        loadingLiveData.value = true
        val params = HashMap<String, String>()
        var id: Int

        id = sessionManager.sharedPreferences.getInt(PreferenceKey.SHOP_ID, Int.MAX_VALUE)

        getCompositeDisposable().addAll(shopRepository.editRestaurant(id, object : ApiListener {
            override fun success(successData: Any) {

                editRestaurantResponse.value = successData as EditRestaurantModel
                loadingLiveData.value = false
            }

            override fun fail(failData: Throwable) {

                commanMethods.getErrorMessage(failData)
                loadingLiveData.value = false
            }
        }))
    }

    fun postValue() {

        loadingLiveData.value = true
        var id: Int
        id = sessionManager.sharedPreferences.getInt(PreferenceKey.SHOP_ID, Int.MAX_VALUE)

        val params = HashMap<String, String>()
        params[WebApiConstants.EditRestaurant.SHOPNAME] = shopName.value.toString()
        params[WebApiConstants.EditRestaurant.COUNTRYCODE] = country_code.value.toString()
        params[WebApiConstants.EditRestaurant.COUNTRY_ID] = country_id
        params[WebApiConstants.EditRestaurant.CITY] = city.value!!
        params[WebApiConstants.EditRestaurant.CITY_ID] = city_id
        params[WebApiConstants.EditRestaurant.EMAIL] = email.value.toString()
        params[WebApiConstants.EditRestaurant.PHONENUMBER] = phoneNumber.value.toString()
        params[WebApiConstants.EditRestaurant.STATUS] = "1"

        // params[WebApiConstants.EditRestaurant.DISHIMAGE] = ""
        //    params[WebApiConstants.EditRestaurant.CUISINE_ID+"[]"] =cuisineIds
        //    params[WebApiConstants.EditRestaurant.CUISINE+"[]"] =cuisineIds
        params[WebApiConstants.EditRestaurant.STORE_GST] = gst.value!!
        params[WebApiConstants.EditRestaurant.COMMISSION] = commission.value!!
        params[WebApiConstants.EditRestaurant.STORE_PACKAGING_CHARGES] = storePackageCharge.value!!
        params[WebApiConstants.EditRestaurant.OFFER_MIN_AMOUNT] = minimumAmount.value!!
        params[WebApiConstants.EditRestaurant.OFFER_PERCENT] = offer.value!!
        params[WebApiConstants.EditRestaurant.ESTIMATED_DELIVERY_TIME] = maximumDeliveryTime.value!!
        params[WebApiConstants.EditRestaurant.DESCRIPTION] = description.value!!
        params[WebApiConstants.EditRestaurant.LANDMARK] = landMark.value!!
        params[WebApiConstants.EditRestaurant.IS_PURE_VEG] = is_veg.value!!
        params[WebApiConstants.EditRestaurant.STATUS] = selectedStatus
        params[WebApiConstants.EditRestaurant._METHOD] = "PATCH"
        params[WebApiConstants.EditRestaurant.LATITUDE] = "" + latitude.value
        params[WebApiConstants.EditRestaurant.LONGITUDE] = "" + longitude.value
        params[WebApiConstants.EditRestaurant.STORE_ZIPCODE] = zipCode.value!!
        params[WebApiConstants.EditRestaurant.ZONE_ID] = zoneid
        params[WebApiConstants.EditRestaurant.STORE_RESPONSE_TIME] = stroeResponseTime.value!!
        params[WebApiConstants.EditRestaurant.CONTACT_PERSON] = contactPerson.value!!
        params[WebApiConstants.EditRestaurant.STREET] = street.value!!
        params[WebApiConstants.EditRestaurant.FLATNO] = flatno.value!!

        params[WebApiConstants.EditRestaurant.STORE_TYPE_ID] =
            "" + sessionManager.sharedPreferences.getInt(PreferenceKey.SHOP_TYPE, Int.MAX_VALUE)

        getCompositeDisposable().addAll(
            shopRepository.editRestaurant(
                params,
                cuisineIds,
                id,
                object : ApiListener {
                    override fun success(successData: Any) {
                        // editRestaurantResponse.postValue(successData as EditRestaurantModel)
                        commonSuccessResponse.value = successData as CommonSuccessResponse
                        loadingLiveData.value = false
                        textWatch = true
                        // Log.d("ProfileViewModel", successData.message.toString())
                    }

                    override fun fail(failData: Throwable) {
                        navigator.showError(getErrorMessage(failData))
                        loadingLiveData.value = false
                        Log.e("ProfileViewModel", failData.message.toString())

                    }
                })
        )

    }

    fun getCountryList() {
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["salt_key"] = SALT_KEY1
        getCompositeDisposable().add(shopRepository.getCountryList(object : ApiListener {
            override fun success(successData: Any) {
                countryListResponse.value = successData as CountryListResponse
            }

            override fun fail(failData: Throwable) {
                navigator.showError(failData.toString())
            }
        }, hashMap))
    }
}