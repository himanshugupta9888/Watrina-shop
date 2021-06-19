package com.watirna.shop.datamodel.product


data class ProductItemsModel(
    val message: String? = "",
    val responseData: ResponseData,
    val statusCode: String? = "",
    val title: String? = ""
) {
    data class ResponseData(var data: List<Data>,var next_page_url:String? = "") {
        var to: Int? = 0
        var total: Int? = 0
        data class Data(
            var id: Int,
            var store_id: Int,
            var item_name: String,
            var item_description: String,
            var store_category_id: Int,
            var picture:String
        )
    }
}