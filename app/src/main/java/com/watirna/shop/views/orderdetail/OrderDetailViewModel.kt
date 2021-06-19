package com.watirna.shop.views.orderdetail

import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.HistoryDetailModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import javax.inject.Inject

class OrderDetailViewModel : BaseViewModel<OrderDetailNavigator>() {
    var orderDetailResponse = MutableLiveData<HistoryDetailModel>()
    var takeAwayResponse = MutableLiveData<CommonSuccessResponse>()
    var mLoadingProgress = MutableLiveData<Boolean>()
    var orderID = MutableLiveData<Int>()


    init {
        AppController.appComponent.inject(this)
    }

    @Inject
    lateinit var shopRepository: ShopRepository

    fun callOrderDetail() {
        if (orderID.value != null)
            shopRepository.getOrderDetail(orderID.value!!, object : ApiListener {
                override fun success(successData: Any) {
                    orderDetailResponse.postValue(successData as HistoryDetailModel)
                }

                override fun fail(failData: Throwable) {
                }

            })
    }

    fun callTakeAway(id: String, store_id: String, status: String) {
        if (orderID.value != null)
            shopRepository.takeAway(id, store_id, status, object : ApiListener {
                override fun success(successData: Any) {
                    mLoadingProgress.value = false
                    takeAwayResponse.postValue(successData as CommonSuccessResponse)
                }

                override fun fail(failData: Throwable) {
                    mLoadingProgress.value = false
                }

            })
    }
}