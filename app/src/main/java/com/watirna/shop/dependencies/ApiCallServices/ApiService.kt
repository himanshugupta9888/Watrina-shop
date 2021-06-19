package com.watirna.shop.dependencies.ApiCallServices

import com.watirna.shop.datamodel.*
import com.watirna.shop.datamodel.request.EditTimingRequest
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.LoginModel
import com.watirna.shop.datamodel.NewOrderModel
import com.watirna.shop.datamodel.addon.AddOnDataModel
import com.watirna.shop.datamodel.addon.AddOnResponseModel
import com.watirna.shop.datamodel.category.CategoryItemsModel
import com.watirna.shop.datamodel.category.SingleCategoryModel
import com.watirna.shop.datamodel.product.CuisineModel
import com.watirna.shop.datamodel.product.EditProductModel
import com.watirna.shop.datamodel.product.ProductItemsModel
import com.watirna.shop.views.forgotPasswordActivity.ForgotPasswordResponse
import com.watirna.shop.views.forgotPasswordActivity.ResetPasswordResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import kotlin.collections.HashMap

interface ApiService {
    @FormUrlEncoded
    @POST("api/v1/shop/login")
    fun postLogin(@FieldMap params: HashMap<String, String>): Observable<LoginModel>

    @GET("api/v1/shop/dispatcher/orders")
    fun getIncomingOrders(
        @Query("type") type: String
    ): Observable<NewOrderModel>


    @POST("api/v1/shop/logout")
    fun postLogout(): Observable<CommonSuccessResponse>

    @FormUrlEncoded
    @POST("api/v1/shop/dispatcher/accept")
    fun acceptOrder(@FieldMap params: HashMap<String, String>): Observable<CommonSuccessResponse>


    @FormUrlEncoded
    @POST("api/v1/shop/dispatcher/cancel")
    fun cancelOrder(@FieldMap params: HashMap<String, String>): Observable<CommonSuccessResponse>


    @FormUrlEncoded
    @POST("api/v1/shop/password")
    fun changePassword(@FieldMap params: HashMap<String, String>): Observable<CommonSuccessResponse>

    @FormUrlEncoded
    @POST("api/v1/shop/forgotOtp")
    fun forgotPassword(@FieldMap params: HashMap<String, String>): Observable<ForgotPasswordResponse>

    @FormUrlEncoded
    @POST("api/v1/shop/resetOtp")
    fun resetPassword(@FieldMap params: HashMap<String, String>): Observable<ResetPasswordResponse>

    @GET("api/v1/shop/shopstiming")
    fun editTiming(): Observable<ShopTimeModel>

    @GET("api/v1/shop/total/storeorder")
    fun getRevenueData(): Single<RevenueResponse>

    @POST("api/v1/shop/shopstiming")
    fun addUpdateShopTimings(@Body request: EditTimingRequest): Observable<ShopTimeModel>

    @GET("/api/v1/shop/shops/{id}")
    fun profile(@Path("id") id: Int): Observable<ProfileModel>

    @GET("/api/v1/shop/requesthistory/{id}")
    fun getOrderDetail(@Path("id") id: Int): Observable<HistoryDetailModel>

    @FormUrlEncoded
    @POST("/api/v1/shop/shops/{id}")
    fun profile(@FieldMap params: HashMap<String, String>): Observable<CommonSuccessResponse>

    @GET("/api/v1/shop/shops/{id}")
    fun editRestaurant(@Path("id") id: Int): Observable<EditRestaurantModel>

    @GET("/api/v1/shop/countries")
    fun getCountries():
            Observable<CountryListResponse>

    @FormUrlEncoded
    @POST("/api/v1/shop/shops/{id}")
    fun editRestaurant(
        @Path("id") id: Int,
        @Field("cuisine_id[]")cuisine_id :ArrayList<Int>,
        @FieldMap params: HashMap<String, String>
    ): Observable<CommonSuccessResponse>

    @GET("api/v1/shop/shoprequesthistory")
    fun getHistoryItem(
        @Query("type") type: String,
        @Query("limit") limit: String
    ): Observable<HistoryDataModel>

    @GET("api/v1/shop/shoprequesthistory")
    fun getHistoryItemOngoing(
        @Query("type") type: String,
        @Query("limit") limit: String
    ): Observable<HistoryDataModel>

    @POST("api/v1/shop/dispatcher/pickedup ")
    fun TakeAway(
        @Query("id") id: String,
        @Query("store_id") store_id: String,
        @Query("status") status: String
    ): Observable<CommonSuccessResponse>

    @GET("api/v1/shop/addonslist/{id}")
    fun getAddOnList(@Path("id") id: Int): Observable<AddOnResponseModel>

    @DELETE("api/v1/shop/addons/{id}")
    fun deleteAddOn(@Path("id") id: Int): Observable<CommonSuccessResponse>

    @GET("/api/v1/shop/addons/{id}")
    fun getSingleAddOnItem(@Path("id") id: Int): Observable<AddOnDataModel>

    @FormUrlEncoded
    @POST("/api/v1/shop/addons")
    fun addAddOn(@FieldMap params: HashMap<String, Any>): Observable<CommonSuccessResponse>

    @FormUrlEncoded
    @PATCH("/api/v1/shop/addons/{id}")
    fun editAddOn(
        @Path("id") id: Int,
        @FieldMap params: HashMap<String, Any>
    ): Observable<CommonSuccessResponse>

    @GET("/api/v1/shop/categoryindex/{id}")
    fun getCategoryItems(
        @Path("id") id: Int, @Query("limit") limit: String, @Query("offset") offset: String
    ): Observable<CategoryItemsModel>

    @GET("/api/v1/shop/units")
    fun getUnits(): Observable<UnitsData>

    @DELETE("/api/v1/shop/category/{id}")
    fun deleteCategory(@Path("id") id: Int): Observable<CommonSuccessResponse>

    @Multipart
    @POST("/api/v1/shop/category")
    fun createCategory(
        @PartMap params: HashMap<String, RequestBody>,
        @Part picture: MultipartBody.Part
    ): Observable<CommonSuccessResponse>

    @GET("/api/v1/shop/category/{id}")
    fun getSingleCategory(@Path("id") id: Int): Observable<SingleCategoryModel>

    @Multipart
    @POST("/api/v1/shop/category/{id}")
    fun updateCategory(
        @Path("id") id: Int,
        @PartMap params: HashMap<String, RequestBody>
    ): Observable<CommonSuccessResponse>

    @GET("/api/v1/shop/itemsindex/{id}")
    fun getProductList(
        @Path("id") id: Int,
        @QueryMap hashMap:HashMap<String,Any>,
        @Query("page") page: String
    ): Observable<ProductItemsModel>

    @DELETE("/api/v1/shop/items/{id}")
    fun deleteProduct(@Path("id") id: Int): Observable<CommonSuccessResponse>

    @GET("/api/v1/shop/cuisinelist/{id}")
    fun getCuisineList(@Path("id") id: Int): Observable<CuisineModel>

    @GET("/api/v1/shop/items/{id}")
    fun getProductItem(@Path("id") id: Int): Observable<EditProductModel>

    @Multipart
    @POST("/api/v1/shop/items")
    fun createProduct(
        /*  @Path("id")id: Int,*/
        @Part productImage: MultipartBody.Part,
        @PartMap params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<Int>
    ): Observable<CommonSuccessResponse>

    @Multipart
    @POST("/api/v1/shop/items")
    fun createProductFeature(
        /*  @Path("id")id: Int,*/
        @Part productImage: MultipartBody.Part,
        @Part fImage: MultipartBody.Part,
        @PartMap params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_price: ArrayList<Int>,
        cuisineId: ArrayList<Int>
    ): Observable<CommonSuccessResponse>

    @Multipart
    @POST("/api/v1/shop/items/{id}")
    fun updateProduct(
        @Path("id") id: Int,
        @Part productImage: MultipartBody.Part,
        @PartMap params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<String>
    ): Observable<CommonSuccessResponse>

    @Multipart
    @POST("/api/v1/shop/items/{id}")
    fun updateProduct(
        @Path("id") id: Int,
        @PartMap params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_prices: ArrayList<String>
    ): Observable<CommonSuccessResponse>

    @Multipart
    @POST("/api/v1/shop/items/{id}")
    fun updateProductFeature(
        @Path("id") id: Int,
        @Part productImage: MultipartBody.Part,
        @Part fImage: MultipartBody.Part,
        @PartMap params: HashMap<String, RequestBody>,
        @Part("addon[]") addons: ArrayList<Int>,
        @Part("addon_price[]") addon_price: ArrayList<String>
    ): Observable<CommonSuccessResponse>

}