package com.watirna.shop.views.revenue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.watirna.shop.application.AppController
import com.watirna.shop.datamodel.RevenueData
import com.watirna.shop.datamodel.RevenueResponse
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.CommanMethods
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RevenueViewModel : ViewModel() {

    @Inject
    lateinit var shopRepository: ShopRepository

    @Inject
    lateinit var commonMethods: CommanMethods

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var loadingLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()
    var revenueLiveData = MutableLiveData<RevenueData>()

    init {
        AppController.appComponent.inject(this)
    }

    fun getRevenueResponse() {

        loadingLiveData.value = true

        compositeDisposable.add(shopRepository.getRevenue(object : ApiListener {
            override fun success(successData: Any) {
               val data = successData as RevenueResponse
                if (data.statusCode=="200")
                revenueLiveData.value =data.responseData

                loadingLiveData.value = false
            }

            override fun fail(failData: Throwable) {
                errorLiveData.value = commonMethods.getErrorMessage(failData)
                loadingLiveData.value = false
            }
        }))

    }


    fun getRevenue(): LiveData<RevenueData> {
        return revenueLiveData
    }

    fun getLoadState(): LiveData<Boolean> {
        return loadingLiveData
    }

    fun getError(): LiveData<String> {
        return errorLiveData
    }
}