package com.watirna.shop.datamodel.product

data class CuisineModel(
    val message: String? = "",
    val responseData: List<ResponseData>,
    val statusCode: String? = "",
    val title: String? = ""
) {
    data class ResponseData(
        var id: Int,
        var store_type_id: Int,
        var name: String,
        var status: String
    )
}