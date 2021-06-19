package com.watirna.shop.views.product.category.edit

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.category.SingleCategoryModel
import com.watirna.shop.dependencies.ApiCallServices.ApiService
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject

class EditCategoryViewModel : BaseViewModel<EditCategoryNavigator>() {
    var categoryName = ObservableField<String>()
    var category_desc = ObservableField<String>()
    var singleCategoryResponse = MutableLiveData<SingleCategoryModel>()
    var errorResponse = MutableLiveData<String>()
    var commonSuccessResponse = MutableLiveData<CommonSuccessResponse>()

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var shopRepository: ShopRepository

    init {
        AppController.appComponent.inject(this)
    }


    fun getSingleCategory(id: Int) {
        getCompositeDisposable().add(shopRepository.getSingleCategory(id, object : ApiListener {
            override fun success(successData: Any) {
                singleCategoryResponse.value = successData as SingleCategoryModel
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = "" + failData.message.toString()
            }

        }))
    }

    fun updateCategory() {
        navigator.updateCategoryMethod()
    }

    fun imageUpload() {
        navigator.selectImage()
    }

    fun updateCategoryApi(id: Int, params: HashMap<String, RequestBody>) {
        params.put(
            "store_id",
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                "" + sessionManager.sharedPreferences.getInt(
                    PreferenceKey.SHOP_ID,
                    0
                )
            )
        )
        getCompositeDisposable().add(
            shopRepository.updateCategory(
                id, params,
                object : ApiListener {
                    override fun success(successData: Any) {
                        commonSuccessResponse.value = successData as CommonSuccessResponse
                    }

                    override fun fail(failData: Throwable) {
                        errorResponse.value = "" + failData.message.toString()
                    }

                })
        )
    }
}