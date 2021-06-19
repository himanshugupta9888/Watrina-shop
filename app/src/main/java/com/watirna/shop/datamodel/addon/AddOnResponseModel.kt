package com.watirna.shop.datamodel.addon

data class AddOnResponseModel(
    val message: String? = "",
    val responseData: List<ResponseData> = listOf(),
    val statusCode: String? = "",
    val title: String? = ""
) {
    data class ResponseData(
        var id: Int, var store_i: Int,
        var addon_name: String,
        var addon_status: Int,
        var storeitem: Any,
        var selection: Boolean = false,
        var price:Double=0.0
    )
}