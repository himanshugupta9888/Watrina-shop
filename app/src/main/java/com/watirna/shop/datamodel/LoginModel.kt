package com.watirna.shop.datamodel

data class LoginModel(
    val error: List<Any?>? = listOf(),
    val message: String? = "",
    val responseData: ResponseData? = ResponseData(),
    val statusCode: String? = "",
    val title: String? = ""
) {
    data class ResponseData(
        val access_token: String? = "",
        val expires_in: Int? = 0,
        val token_type: String? = "",
        val user: User? = User()
    ) {
        data class User(
            val city_id: Int? = 0,
            val commission: Int? = 0,
            val contact_number: String? = "",
            val contact_person: String? = "",
            val country_code: Int? = 0,
            val country_id: Int? = 0,
            val currency: String? = "",
            val currency_symbol: String? = "",
            val description: String? = "",
            val device_id: String? = "",
            val device_token: String? = "",
            val device_type: String? = "",
            val email: String? = "",
            val estimated_delivery_time: String? = "",
            val free_delivery: Int? = 0,
            val id: Int? = 0,
            val is_bankdetail: Int? = 0,
            val is_veg: String? = "",
            val language: String? = "",
            val latitude: Double? = 0.0,
            val longitude: Double? = 0.0,
            val offer_min_amount: String? = "",
            val offer_percent: Int? = 0,
            val otp: Any? = Any(),
            val password: String? = "",
            val picture: String? = "",
            val rating: Any? = Any(),
            val status: Int? = 0,
            val store_gst: Int? = 0,
            val store_location: String? = "",
            val store_name: String? = "",
            val store_packing_charges: Int? = 0,
            val store_type_id: Int? = 0,
            val store_zipcode: String? = "",
            val wallet_balance: Double? = 0.0,
            val store_type: String="",
            val zone_id: Int? = 0
        )
    }
}