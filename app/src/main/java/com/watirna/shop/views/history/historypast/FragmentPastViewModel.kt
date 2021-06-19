package com.watirna.shop.views.history.historypast

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.HistoryDataModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.Constants
import javax.inject.Inject
import kotlin.math.roundToInt

class FragmentPastViewModel : BaseViewModel<FragmentPastNavigator>() {
    var pageNumber = MutableLiveData<Int>(1)
    var limit = MutableLiveData<String>("10")
    var pastOrdersList =  MutableLiveData<ObservableArrayList<HistoryDataModel.ResponseData.Data>>(ObservableArrayList())
    var mlProgress = MutableLiveData<Boolean>()
    var totalItem = MutableLiveData<Int>()
    var totalPage = MutableLiveData<Int>()

    init {
        AppController.appComponent.inject(this)
    }

    @Inject
    lateinit var shopRepository: ShopRepository

    fun getPastOrderList() {
        shopRepository.getHistoyList(
            limit.value.toString(),
            pageNumber.value.toString(),
            Constants.WebConstants.COMPLETED,
            object : ApiListener {
                override fun success(successData: Any) {
                    mlProgress.value = false
                    val tempOnGoingList = successData as HistoryDataModel
                    if (tempOnGoingList.responseData != null && tempOnGoingList.responseData!!.data.size > 0) {
                        if (pageNumber.value == 1) {
                            val totalPagesCount: Double =
                                (tempOnGoingList.responseData!!.total!!.toDouble() / 10)
                            totalPage.value = totalPagesCount.roundToInt()
                            totalItem.value = tempOnGoingList.responseData!!.total

                        }
                        pastOrdersList.value!!.addAll(tempOnGoingList.responseData!!.data)
                    }
                }

                override fun fail(failData: Throwable) {
                    mlProgress.value = false
                }

            })
    }
}