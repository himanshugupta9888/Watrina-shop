package com.watirna.shop.views.requestdetail

import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.NewOrderModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.utils.WebApiConstants
import javax.inject.Inject

class RequestDetailViewModel : BaseViewModel<RequestDetailNavigator>() {

    @Inject
    lateinit var shopRepository: ShopRepository

    @Inject
    lateinit var  sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }

    var incomingOrderModelLd = MutableLiveData<NewOrderModel.ResponseData>()
    var commonRespnoseLd = MutableLiveData<CommonSuccessResponse>()
    var preparationTime = MutableLiveData<String>()
    var storeID = MutableLiveData<String>()
    var userID = MutableLiveData<String>()
    var loadingProgressLd=MutableLiveData<Boolean>()


    fun acceptOrders() {
        navigator.acceptOrder()
    }

    fun cancelOrders() {
        navigator.cancelOrder()
    }


    fun acceptOrder() {
        loadingProgressLd.value=true
        val params = HashMap<String, String>()
        params[WebApiConstants.OrderAccept.STOREID] = storeID.value.toString()
        params[WebApiConstants.OrderAccept.USERID] = userID.value.toString()
        if(sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME,"").equals("FOOD"))
            params[WebApiConstants.OrderAccept.COOKTIME] = preparationTime.value.toString()
        else
            params[WebApiConstants.OrderAccept.DELIVERYDATE] = preparationTime.value.toString()
        getCompositeDisposable().addAll(shopRepository.accpetOrder(params, object : ApiListener {
            override fun success(successData: Any) {
                loadingProgressLd.value=false
                commonRespnoseLd.value = successData as CommonSuccessResponse

            }

            override fun fail(failData: Throwable) {
                loadingProgressLd.value=false
                navigator.getErrorMessage(getErrorMessage(failData))
            }

        }))

    }

    fun cancelOrder(cancelReason: String) {
        loadingProgressLd.value=true
        val params = HashMap<String, String>()
        params[WebApiConstants.OrderAccept.ID] = storeID.value.toString()
        params[WebApiConstants.OrderAccept.USERID] = userID.value.toString()
        val storeID=sessionManager.get<Int>(PreferenceKey.SHOP_ID)
        params[WebApiConstants.OrderAccept.SHOPID] = storeID.toString()
        params["cancel_reason"] = cancelReason
        getCompositeDisposable().addAll(shopRepository.cancelOrder(params, object : ApiListener {
            override fun success(successData: Any) {
                loadingProgressLd.value=false
                commonRespnoseLd.value = successData as CommonSuccessResponse
            }

            override fun fail(failData: Throwable) {
                loadingProgressLd.value=false
                navigator.getErrorMessage(getErrorMessage(failData))
            }

        }))
    }
}