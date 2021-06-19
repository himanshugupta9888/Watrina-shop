package com.watirna.shop.views.product.category

import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonErrorResponse
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.category.CategoryItemsModel
import com.watirna.shop.dependencies.ApiCallServices.ApiService
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import javax.inject.Inject

class CategoriesViewModel : BaseViewModel<CategoriesNavigator>() {

    var categoryItemsModel = MutableLiveData<CategoryItemsModel>()
    var commonSuccessResponse = MutableLiveData<CommonSuccessResponse>()
    var errorResponse = MutableLiveData<CommonErrorResponse>()

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var shopRepository: ShopRepository


    init {
        AppController.appComponent.inject(this)
        getCategoryList()
    }

    fun getCategoryList() {
        getCompositeDisposable().add(
            shopRepository.getCategoryList(
                sessionManager.sharedPreferences.getInt(
                    PreferenceKey.SHOP_ID,
                    0
                ), "10", "0", object : ApiListener {
                    override fun success(successData: Any) {
                        categoryItemsModel.value = successData as CategoryItemsModel
                    }

                    override fun fail(failData: Throwable) {

                    }

                })
        )
    }

    fun deleteAddOn(id: Int) {
        getCompositeDisposable().add(shopRepository.deleteCategory(id, object : ApiListener {
            override fun success(successData: Any) {
                commonSuccessResponse.value = successData as CommonSuccessResponse
            }

            override fun fail(failData: Throwable) {
                 
            }

        }))
    }

    fun goToCreateCategory() {
        navigator.openCreateCategoryActivity()
    }

    fun openEditActivity(id: Int) {
        navigator.openEditActivity(id)
    }
}