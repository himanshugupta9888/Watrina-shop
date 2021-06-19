package com.watirna.shop.datamodel

data class CommonErrorResponse(
    val error: List<Any?>? = listOf(),
    val message: String? = "",
    val responseData: List<Any?>? = listOf(),
    val statusCode: String? = "",
    val title: String? = ""
)