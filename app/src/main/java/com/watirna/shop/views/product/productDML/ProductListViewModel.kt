package com.watirna.shop.views.product.productDML

import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.product.ProductItemsModel
import com.watirna.shop.dependencies.ApiCallServices.ApiService
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import javax.inject.Inject

class ProductListViewModel : BaseViewModel<ProductListNavigator>() {
    var commonSuccessResponse = MutableLiveData<CommonSuccessResponse>()
    var productListItems = MutableLiveData<ProductItemsModel>()
    var loadingLiveData = MutableLiveData<Boolean>()

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var shopRepository: ShopRepository

    init {
        AppController.appComponent.inject(this)
    }


    fun apiCallProducts(hashMap: HashMap<String, Any>, page : Int) {
        /* hashMap.put(
             "store_id", sessionManager.sharedPreferences.getInt(
                 PreferenceKey.SHOP_ID,
                 0
             )
         )*/
        loadingLiveData.value = true
        getCompositeDisposable().add(
            shopRepository.getProductList(
                object : ApiListener {
                    override fun success(successData: Any) {
                        //  commonSuccessResponse.value = successData as CommonSuccessResponse
                        loadingLiveData.value = false
                        productListItems.value = successData as ProductItemsModel
                    }

                    override fun fail(failData: Throwable) {
                        loadingLiveData.value = false
                    }

                }, sessionManager.sharedPreferences.getInt(
                    PreferenceKey.SHOP_ID,
                    0
                ), hashMap,page.toString()
            )
        )
    }

    fun deleteProduct(id: Int) {
        loadingLiveData.value = true

        getCompositeDisposable().add(
            shopRepository.deletProduct(
                object : ApiListener {
                    override fun success(successData: Any) {
                        loadingLiveData.value = false
                         commonSuccessResponse.value = successData as CommonSuccessResponse
                    }

                    override fun fail(failData: Throwable) {
                        loadingLiveData.value = false
                    }

                }, id)
        )
    }
    fun openEditProductActivity(id:Int){
        navigator.openEditProductActivity(id)
    }

    fun openCreateProductActivity(){
        navigator.openCreateProductActivity()
    }
}