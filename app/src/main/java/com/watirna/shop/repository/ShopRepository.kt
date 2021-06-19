package com.watirna.shop.repository

import com.watirna.shop.application.AppController
import com.watirna.shop.dependencies.ApiCallServices.ApiService
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.datamodel.request.EditTimingRequest
import com.watirna.shop.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import javax.inject.Inject

class ShopRepository {

    @Inject
    lateinit var apiService: ApiService

    init {
        AppController.appComponent.inject(this)
    }


    fun callLoginApi(params: HashMap<String, String>, apiListener: ApiListener): Disposable {
        return apiService.postLogin(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)

            }, {
                apiListener.fail(it)
            })
    }


    fun getIncomingOrders(params: HashMap<String, String>, apiListener: ApiListener): Disposable {
        return apiService.getIncomingOrders(Constants.WebConstants.ORDERED)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            }, {

            })
    }


    fun logout(apiListener: ApiListener): Disposable {
        return apiService.postLogout()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })
    }

    fun forgotPassword(params: HashMap<String, String>, apiListener: ApiListener): Disposable {
        return apiService.forgotPassword(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            }, {

            })
    }

    fun resetPassword(params: HashMap<String, String>, apiListener: ApiListener): Disposable {
        return apiService.resetPassword(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            }, {

            })
    }

    fun accpetOrder(params: HashMap<String, String>, apiListener: ApiListener): Disposable {
        return apiService.acceptOrder(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })
    }

    fun cancelOrder(params: HashMap<String, String>, apiListener: ApiListener): Disposable {
        return apiService.cancelOrder(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })
    }

    fun changePassword(params: HashMap<String, String>, apiListener: ApiListener): Disposable {
        return apiService.changePassword(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })
    }

    fun takeAway(id: String, store_id: String, status: String, apiListener: ApiListener): Disposable {
        return apiService.TakeAway(id, store_id, status)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })
    }

    fun getHistoyList(
        limit: String,
        pageNumber: String,
        type: String,
        apiListener: ApiListener
    ): Disposable {
        return apiService.getHistoryItem(type, limit)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })
    }

    fun getHistoyListOngoing(
        limit: String,
        pageNumber: String,
        type: String,
        apiListener: ApiListener
    ): Disposable {
        return apiService.getHistoryItemOngoing(type,limit)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })
    }

    fun getShopTiming(params: HashMap<String, String>, apiListener: ApiListener): Disposable {
        return apiService.editTiming().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })


    }

    fun getRevenue(apiListener: ApiListener): Disposable {
        return apiService.getRevenueData().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })


    }

    fun saveShopTiming(req: EditTimingRequest, apiListener: ApiListener): Disposable {
        return apiService.addUpdateShopTimings(req).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })


    }

    fun getProfile(
        params: HashMap<String, String>,
        id: Int, apiListener: ApiListener
    ): Disposable {

        return apiService.profile(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun getCountryList(listener: ApiListener, params: HashMap<String, Any?>): Disposable {
        return apiService.getCountries().observeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ listener.success(it) }, { listener.fail(it) })
    }

    fun editRestaurant(
        params: HashMap<String, String>,
        cuisine_id :ArrayList<Int>,
        id: Int, apiListener: ApiListener
    ): Disposable {

        return apiService.editRestaurant(id, cuisine_id, params).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun editRestaurant(
        id: Int, apiListener: ApiListener
    ): Disposable {

        return apiService.editRestaurant(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    //addonlist
    fun getAddonList(
        id: Int, apiListener: ApiListener
    ): Disposable {
        return apiService.getAddOnList(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun deleteAddon(id: Int, apiListener: ApiListener): Disposable {
        return apiService.deleteAddOn(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    //Create Addon
    fun addAddOn(apiListener: ApiListener, params: HashMap<String, Any>): Disposable {
        return apiService.addAddOn(params).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun getSingelAddOnItem(apiListener: ApiListener, addOnId: Int): Disposable {
        return apiService.getSingleAddOnItem(addOnId).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    //Edit Addon
    fun editAddOn(
        apiListener: ApiListener,
        addOnId: Int,
        params: HashMap<String, Any>
    ): Disposable {
        return apiService.editAddOn(addOnId, params).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun getCategoryList(
        shopId: Int,
        limit: String,
        offset: String,
        apiListener: ApiListener
    ): Disposable {
        return apiService.getCategoryItems(shopId, limit, offset)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun getUnits(shopId: Int, apiListener: ApiListener): Disposable {
        return apiService.getUnits()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun deleteCategory(id: Int, apiListener: ApiListener): Disposable {
        return apiService.deleteCategory(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }


    fun createCategory(
        params: HashMap<String, RequestBody>,
        file: MultipartBody.Part,
        apiListener: ApiListener
    ): Disposable {
        return apiService.createCategory(params, file).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun getSingleCategory(categoryId: Int, apiListener: ApiListener): Disposable {
        return apiService.getSingleCategory(categoryId).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun updateCategory(
        id: Int,
        params: HashMap<String, RequestBody>,
        apiListener: ApiListener
    ): Disposable {
        return apiService.updateCategory(id, params).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun getOrderDetail(id: Int, apiListener: ApiListener): Disposable {
        return apiService.getOrderDetail(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })
    }

    fun getProductList(
        apiListener: ApiListener,
        id: Int,
        hashMap: HashMap<String, Any>,
        page: String
    ): Disposable {
        return apiService.getProductList(id, hashMap, page).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun deletProduct(apiListener: ApiListener, id: Int): Disposable {
        return apiService.deleteProduct(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun getCuisineList(id: Int, apiListener: ApiListener): Disposable {
        return apiService.getCuisineList(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun createProduct(
        id: Int,
        productImage: MultipartBody.Part,
        params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<Int>,
        @Part("cuisine_id[]") cuisine_id:ArrayList<Int>,
        apiListener: ApiListener
    ): Disposable {
        return apiService.createProduct(productImage, params, addons, addon_prices)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun createProduct(
        id: Int,
        productImage: MultipartBody.Part,
        fImage: MultipartBody.Part,
        params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<Int>,
        @Part("cuisine_id[]") cuisine_id:ArrayList<Int>,
        apiListener: ApiListener
    ): Disposable {
        return apiService.createProductFeature(productImage, fImage, params, addons, addon_prices,cuisine_id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun updateProduct(
        id: Int,
        productImage: MultipartBody.Part,
        params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<String>,
        apiListener: ApiListener
    ): Disposable {
        return apiService.updateProduct(id,productImage, params, addons, addon_prices)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun updateProduct(
        id: Int,
        params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<String>,
        apiListener: ApiListener
    ): Disposable {
        return apiService.updateProduct(id, params, addons, addon_prices)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun updateProduct(
        id: Int,
        productImage: MultipartBody.Part,
        fImage: MultipartBody.Part,
        params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<String>,
        apiListener: ApiListener
    ): Disposable {
        return apiService.updateProductFeature(id,productImage, fImage, params, addons, addon_prices)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)

            })
    }

    fun getProductDetails(id: Int, apiListener: ApiListener): Disposable {
        return apiService.getProductItem(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                apiListener.success(it)
            }, {
                apiListener.fail(it)
            })
    }

}


