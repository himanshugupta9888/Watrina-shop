package com.watirna.shop.datamodel

import java.io.Serializable

data class UnitsData(
    val error: List<Any>,
    val message: String,
    val responseData: List<UnitsResponseData>,
    val statusCode: String,
    val title: String
):Serializable

data class UnitsResponseData(
    val id: Int,
    val name: String,
    val company_id: Int
):Serializable