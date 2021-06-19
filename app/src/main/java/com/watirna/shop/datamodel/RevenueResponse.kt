package com.watirna.shop.datamodel

data class RevenueResponse(
    var error: List<Any> = listOf(),
    var message: String = "",
    var responseData: RevenueData = RevenueData(),
    var statusCode: String = "",
    var title: String = ""
)