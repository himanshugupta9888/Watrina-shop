package com.watirna.shop.views.orders.incoming

import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonErrorResponse
import com.watirna.shop.datamodel.NewOrderModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.Constants
import javax.inject.Inject

class IncomingViewModel : BaseViewModel<IncomingNavigator>() {

    var errorResponse=MutableLiveData<CommonErrorResponse>()
    var newOrderLiveData = MutableLiveData<NewOrderModel>()

    @Inject
    lateinit var shopRepository: ShopRepository


    init {
        AppController.appComponent.inject(this)
    }


    fun getIncomingOrders() {
        val params= HashMap<String,String>(
        )
        params["type"]=Constants.WebConstants.ORDERED
        getCompositeDisposable().addAll(shopRepository.getIncomingOrders(params,
            object : ApiListener {
                override fun success(successData: Any) {
                    newOrderLiveData.value = successData as NewOrderModel
                }

                override fun fail(failData: Throwable) {
                    //navigator.showError(getErrorMessage(failData))
                      navigator.showError(getErrorMessage(failData),getStatusCode(failData))
                }
            }))
    }


}