package com.watirna.shop.views.product.productDML.edit.editproduct

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.UnitsData
import com.watirna.shop.datamodel.addon.KeyValuePair
import com.watirna.shop.datamodel.category.CategoryItemsModel
import com.watirna.shop.datamodel.product.CuisineModel
import com.watirna.shop.datamodel.product.EditProductModel
import com.watirna.shop.dependencies.ApiCallServices.ApiService
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import javax.inject.Inject

class EditProductViewModel : BaseViewModel<EditProductNavigator>() {


    var categoryItemsModel = MutableLiveData<CategoryItemsModel>()
    var cuisineModel = MutableLiveData<CuisineModel>()
    var commonSuccessResponse = MutableLiveData<CommonSuccessResponse>()
    var unitsModel = MutableLiveData<UnitsData>()


    var productName = ObservableField<String>()
    var descriptionString = ObservableField<String>()
    var productCuisine = ObservableField<String>()
    var productOrder = ObservableField<String>()
    var categroyString = ObservableField<String>()
    var cuisuieName = ObservableField<String>()

    var price = ObservableField<String>("")
    var discount = ObservableField<String>("")
    var percentage = ObservableArrayList<KeyValuePair>()

    var addOnIdArray = ArrayList<String>()
    var addOnPrice = ArrayList<String>()
    var editProductReponse= MutableLiveData<EditProductModel>()
    var quantity=ObservableField<String>()
    var selectedUnit = "0"
    var product_Id=""

    var cuisineName = ""
    var cuisineIds = ArrayList<Int>()

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var shopRepository: ShopRepository

    init {
        AppController.appComponent.inject(this)
    }

    init {
        AppController.appComponent.inject(this)
        getCategoryList()
       // getCuisineList()
    }


    fun getCategoryList() {
        getCompositeDisposable().add(
            shopRepository.getCategoryList(
                sessionManager.sharedPreferences.getInt(
                    PreferenceKey.SHOP_ID,
                    0
                ), "100", "0", object : ApiListener {
                    override fun success(successData: Any) {
                        categoryItemsModel.value = successData as CategoryItemsModel
                    }

                    override fun fail(failData: Throwable) {

                    }

                })
        )
    }

    fun getUnits() {
        getCompositeDisposable().add(shopRepository.getUnits(sessionManager.sharedPreferences.getInt(PreferenceKey.SHOP_ID, 0), object : ApiListener {
            override fun success(successData: Any) {
                unitsModel.value = successData as UnitsData
            }

            override fun fail(failData: Throwable) {

            }

        })
        )
    }

    fun getCuisineList() {
        getCompositeDisposable().add(
            shopRepository.getCuisineList(
                sessionManager.sharedPreferences.getInt(
                    PreferenceKey.SHOP_ID,
                    0
                ), object : ApiListener {
                    override fun success(successData: Any) {
                        cuisineModel.value = successData as CuisineModel
                    }

                    override fun fail(failData: Throwable) {

                    }

                })
        )
    }

    fun createProduct(
        prductImage: MultipartBody.Part,
        params: HashMap<String, RequestBody>, @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<String>
    ) {

        params.put(
            "store_id", RequestBody.create(
                "text/plain".toMediaTypeOrNull(), "" + sessionManager.sharedPreferences.getInt(
                    PreferenceKey.SHOP_ID,
                    0
                )
            )
        )
        /*  params.put("_method", RequestBody.create(
              MediaType.parse("text/plain"),"PATCH"))*/

        getCompositeDisposable().add(
            shopRepository.updateProduct(
               product_Id.toInt(), prductImage, params, addons, addon_prices, object : ApiListener {
                    override fun success(successData: Any) {
                        commonSuccessResponse.value = successData as CommonSuccessResponse
                    }

                    override fun fail(failData: Throwable) {
                        navigator.showError(failData.message!!.toString())
                    }

                })
        )
    }


    fun updateWithouotImage(
        params: HashMap<String, RequestBody>, @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<String>
    ) {

        params.put(
            "store_id", RequestBody.create(
                "text/plain".toMediaTypeOrNull(), "" + sessionManager.sharedPreferences.getInt(
                    PreferenceKey.SHOP_ID,
                    0
                )
            )
        )
        /*  params.put("_method", RequestBody.create(
              MediaType.parse("text/plain"),"PATCH"))*/

        getCompositeDisposable().add(
            shopRepository.updateProduct(
                product_Id.toInt(), params, addons, addon_prices, object : ApiListener {
                    override fun success(successData: Any) {
                        commonSuccessResponse.value = successData as CommonSuccessResponse
                    }

                    override fun fail(failData: Throwable) {
                        navigator.showError(failData.message!!.toString())
                    }

                })
        )
    }

    fun getProductDetails(){
        getCompositeDisposable().add(
            shopRepository.getProductDetails(
                product_Id.toInt(), object : ApiListener {
                    override fun success(successData: Any) {
                        editProductReponse.value = successData as EditProductModel
                    }

                    override fun fail(failData: Throwable) {
                        navigator.showError(failData.message!!)
                    }

                })
        )
    }

    fun createProductFeature(
        prductImage: MultipartBody.Part,
        fimage: MultipartBody.Part,
        params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<String>
    ) {
        params.put(
            "store_id", RequestBody.create(
                "text/plain".toMediaTypeOrNull(), "" + sessionManager.sharedPreferences.getInt(
                    PreferenceKey.SHOP_ID,
                    0
                )
            )
        )
        /* params.put("_method", RequestBody.create(
             MediaType.parse("text/plain"),"PATCH"))*/
        getCompositeDisposable().add(
            shopRepository.updateProduct(
                product_Id.toInt(), prductImage, fimage, params, addons, addon_prices, object : ApiListener {
                    override fun success(successData: Any) {
                        commonSuccessResponse.value = successData as CommonSuccessResponse
                    }

                    override fun fail(failData: Throwable) {
                        navigator.showError(failData.message!!)
                    }

                })
        )
    }

}