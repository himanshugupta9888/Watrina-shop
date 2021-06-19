package com.watirna.shop.views.history.historyongoing

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.HistoryDataModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.Constants
import javax.inject.Inject

class FragmentOngoingViewModel : BaseViewModel<FragmentOngoingNavigator>() {
    var pageNumber = MutableLiveData<Int>(1)
    var limit = MutableLiveData<Int>(50)
    var ongoingList = MutableLiveData<ObservableArrayList<HistoryDataModel.ResponseData.Data>>(ObservableArrayList())
    var totalItem = MutableLiveData<Int>()
    var totalPage = MutableLiveData<Int>()
    var mLoadingPrgress = MutableLiveData<Boolean>()

    init {
        AppController.appComponent.inject(this)
    }

    @Inject
    lateinit var shopRepository: ShopRepository

    fun getOngoingOrder() {
        mLoadingPrgress.value = true
        shopRepository.getHistoyListOngoing(
            limit.value.toString(),
            pageNumber.value.toString(),
            Constants.WebConstants.ONGOING,
            object : ApiListener {
                override fun success(successData: Any) {
                    mLoadingPrgress.value=false
                    val tempOnGoingList = successData as HistoryDataModel
                    if (tempOnGoingList.responseData != null && tempOnGoingList.responseData!!.data.size > 0) {
                        if (pageNumber.value == 1) {
                            val totalPagesCount: Int = (tempOnGoingList.responseData!!.total!! / 10)
                            totalPage.value = totalPagesCount
                            totalItem.value = tempOnGoingList.responseData!!.total

                        }
                        ongoingList.value!!.addAll(tempOnGoingList.responseData!!.data)
                    }
                }

                override fun fail(failData: Throwable) {
                   mLoadingPrgress.value=false
                }

            })
    }

}