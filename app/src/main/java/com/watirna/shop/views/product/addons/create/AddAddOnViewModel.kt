package com.watirna.shop.views.product.addons.create

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.dependencies.ApiCallServices.ApiService
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import javax.inject.Inject

class AddAddOnViewModel : BaseViewModel<AddAddOnNavigator>() {

    var addOnName = ObservableField<String>("")
    var addOnStatus = ObservableField<Boolean>(false)
    var commonSuccessResponse = MutableLiveData<CommonSuccessResponse>()

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var shopRepository: ShopRepository

    init {
        AppController.appComponent.inject(this)
    }

    fun createAddOn() {
        navigator.createAddFunction()
    }

    fun apiCallAddaddOn(hashMap: HashMap<String, Any>) {
        hashMap.put(
            "store_id", sessionManager.sharedPreferences.getInt(
                PreferenceKey.SHOP_ID,
                0
            )
        )
        getCompositeDisposable().add(shopRepository.addAddOn(object : ApiListener {
            override fun success(successData: Any) {
                commonSuccessResponse.value = successData as CommonSuccessResponse
            }

            override fun fail(failData: Throwable) {

            }

        }, hashMap))
    }
}