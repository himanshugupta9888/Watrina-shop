package com.watirna.shop.views.product.addons

import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.addon.AddOnResponseModel
import com.watirna.shop.dependencies.ApiCallServices.ApiService
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import javax.inject.Inject

class AddOnListViewModel : BaseViewModel<AddOnListNavigator>() {

    var addOnResponse = MutableLiveData<AddOnResponseModel>()
    var commonSuccessResponse = MutableLiveData<CommonSuccessResponse>()

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var shopRepository: ShopRepository

    init {
        AppController.appComponent.inject(this)
        addOnList()
    }

    fun addOnList() {
        getCompositeDisposable().add(
            shopRepository.getAddonList(
                sessionManager.sharedPreferences.getInt(
                    PreferenceKey.SHOP_ID,
                    0
                ), object : ApiListener {
                    override fun success(successData: Any) {
                        addOnResponse.value = successData as AddOnResponseModel
                    }

                    override fun fail(failData: Throwable) {

                    }

                })
        )
    }

    fun deleteAddOn(id: Int) {
        getCompositeDisposable().add(shopRepository.deleteAddon(id, object : ApiListener {
            override fun success(successData: Any) {
                commonSuccessResponse.value = successData as CommonSuccessResponse
            }

            override fun fail(failData: Throwable) {

            }

        }))
    }

    fun openEditActivity(addOnId: Int) {
        navigator.openEditAddOnActivity(addOnId)
    }

    fun goToAddOnCreate() {
        navigator.openAddOnCreateActivity()
    }
}