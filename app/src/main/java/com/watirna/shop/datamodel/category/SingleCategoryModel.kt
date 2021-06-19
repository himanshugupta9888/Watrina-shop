package com.watirna.shop.datamodel.category

data class SingleCategoryModel(
    val message: String? = "",
    val responseData: ResponseData,
    val statusCode: String? = "",
    val title: String? = ""
) {
    data class ResponseData(
        var id: Int,
        var store_id: Int,
        var store_category_name: String,
        var store_category_description: String,
        var picture: String,
        var store_category_status: Int
    ) {
    }
}