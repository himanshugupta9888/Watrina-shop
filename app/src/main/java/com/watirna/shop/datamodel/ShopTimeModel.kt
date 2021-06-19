package com.watirna.shop.datamodel

data class ShopTimeModel(
    var error: List<Any> = arrayListOf(),
    var message: String = "",
    var responseData: List<ResponseData> = arrayListOf(),
    var statusCode: String = "",
    var title: String = ""
) {
    data class ResponseData(
        var id: Int,
        var store_day: String = "",
        var store_end_time: String = "",
        var store_id: Int = 0,
        var store_start_time: String = "",
        var isChecked: Boolean = false,
        var displayName: String = "",
        var error:String=""
    )
}