package com.watirna.shop.datamodel.addon

data class AddOnDataModel(
    val message: String? = "",
    val responseData: AddOnResponseModel.ResponseData,
    val statusCode: String? = "",
    val title: String? = ""
) {
    data class ResponseData(
        var id: Int, var store_i: Int,
        var addon_name: String,
        var addon_status: Int,
        var storeitem: Any
    )
}
