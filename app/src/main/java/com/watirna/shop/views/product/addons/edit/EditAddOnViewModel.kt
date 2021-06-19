package com.watirna.shop.views.product.addons.edit

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.addon.AddOnDataModel
import com.watirna.shop.dependencies.ApiCallServices.ApiService
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import javax.inject.Inject

class EditAddOnViewModel : BaseViewModel<EditAddOnNavigator>() {
    var addOnName = ObservableField<String>("")
    var addOnStatus = ObservableField<Boolean>(false)
    var commonSuccessResponse = MutableLiveData<CommonSuccessResponse>()
    var addOnResonse = MutableLiveData<AddOnDataModel>()

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var shopRepository: ShopRepository

    init {
        AppController.appComponent.inject(this)

    }

    fun getSingleAddOnItem(id: Int) {
        getCompositeDisposable().add(shopRepository.getSingelAddOnItem(object : ApiListener {
            override fun success(successData: Any) {
                addOnResonse.value = successData as AddOnDataModel
            }

            override fun fail(failData: Throwable) {

            }

        }, id))
    }

    fun editAddOn() {
        navigator.EditAddOnFunction()
    }

    fun apiCallAddaddOn(addOnId:Int,hashMap: HashMap<String, Any>) {
        hashMap.put(
            "shop_id", sessionManager.sharedPreferences.getInt(
                PreferenceKey.SHOP_ID,
                0
            )
        )
        getCompositeDisposable().add(shopRepository.editAddOn(object : ApiListener {
            override fun success(successData: Any) {
                commonSuccessResponse.value= successData as CommonSuccessResponse
            }

            override fun fail(failData: Throwable) {

            }

        }, addOnId,hashMap))
    }
}