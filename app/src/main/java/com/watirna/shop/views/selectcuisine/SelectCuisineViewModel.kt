package com.watirna.shop.views.selectcuisine

import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.product.CuisineModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import javax.inject.Inject

class SelectCuisineViewModel :BaseViewModel<SelectCuisineNavigator>() {

    @Inject
    lateinit var shopRepository: ShopRepository

    @Inject
    lateinit var commanMethods: CommanMethods

    @Inject
    lateinit var sessionManager: SessionManager

    var cuisineModel=MutableLiveData<CuisineModel.ResponseData>()

    init {
        AppController.appComponent.inject(this)
    }


    val cuisineResponse = MutableLiveData<CuisineModel>()
    val loadingLiveData = MutableLiveData<Boolean>()


    var southIndian_Foods =MutableLiveData<String>()

    var northIndian_Foods =MutableLiveData<String>()

    var american_Foods =MutableLiveData<String>()

    var arabian_Foods =MutableLiveData<String>()

    var bakers =MutableLiveData<String>()

    var asian =MutableLiveData<String>()

    var african_Foods =MutableLiveData<String>()

    var cuisineIds=ArrayList<Int>()
    var cuisineNames=ArrayList<String>()


    fun selectCuisine() {

        loadingLiveData.value = true
        val params = HashMap<String, String>()
        var id: Int

        id = sessionManager.sharedPreferences.getInt(PreferenceKey.SHOP_TYPE, Int.MAX_VALUE)

        getCompositeDisposable().addAll(shopRepository.getCuisineList(id, object :
            ApiListener {
            override fun success(successData: Any) {

                cuisineResponse.value = successData as CuisineModel
                loadingLiveData.value = false
            }

            override fun fail(failData: Throwable) {

                commanMethods.getErrorMessage(failData)
                loadingLiveData.value = false
            }
        }))
    }


}