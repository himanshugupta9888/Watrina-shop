package com.watirna.shop.views.history.historycancelled

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.HistoryDataModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.Constants
import javax.inject.Inject

class  FragmentCancelViewModel:BaseViewModel<FragmentCancelNavigator>(){
    var pageNumber = MutableLiveData<Int>(1)
    var limit = MutableLiveData<String>("10")
    var cancelList = MutableLiveData<ObservableArrayList<HistoryDataModel.ResponseData.Data>>(ObservableArrayList())
    var totalPages=MutableLiveData<Int>()
    var totalItem=MutableLiveData<Int>()
    var loadingProgress=MutableLiveData<Boolean>(false)

    init {
        AppController.appComponent.inject(this)
    }

    @Inject
    lateinit var shopRepository: ShopRepository

    fun getCancelledOrderList() {
        shopRepository.getHistoyList(
            limit.value.toString(),
            pageNumber.value.toString(),
            Constants.WebConstants.CANCELLED,
            object : ApiListener {
                override fun success(successData: Any) {
                    loadingProgress.value=false
                    val tempOnGoingList = successData as HistoryDataModel
                      if(tempOnGoingList.responseData!=null && tempOnGoingList.responseData!!.data!!.size>0) {
                          if (tempOnGoingList.responseData!!.current_page == 1) {
                              totalPages.value = tempOnGoingList.responseData!!.total!! / 10
                              totalItem.value=tempOnGoingList.responseData!!.total
                          }
                            cancelList.value!!.addAll(tempOnGoingList.responseData!!.data)
                      }
                }

                override fun fail(failData: Throwable) {
                    loadingProgress.value=false
                }

            })
    }
}