package com.watirna.shop.views.product.category.create

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.dependencies.ApiCallServices.ApiService
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateCategoryViewModel : BaseViewModel<CreateCategroyNavigator>() {
    var categoryName = ObservableField<String>()
    var category_desc = ObservableField<String>()

    var commonSuccessResponse = MutableLiveData<CommonSuccessResponse>()
    var errorResponse = MutableLiveData<String>()

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var shopRepository: ShopRepository

    init {
        AppController.appComponent.inject(this)
    }


    fun createCategory() {
        navigator.createCategoryMethod()
    }

    fun imageUpload() {
        navigator.selectImage()
    }

    fun createCategoryApi(params: HashMap<String, RequestBody>, file: MultipartBody.Part) {
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
            shopRepository.createCategory(
                params,
                file,
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