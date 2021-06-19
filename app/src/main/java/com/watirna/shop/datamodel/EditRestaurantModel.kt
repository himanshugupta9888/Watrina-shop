package com.watirna.shop.datamodel

data class EditRestaurantModel(
    val error: List<Any>,
    val message: String,
    val responseData: ResponseData,
    val statusCode: String,
    val title: String
) {
    data class ResponseData(
        val city_data: List<CityData>,
        val city_id: Int,
        val commission: Int,
        val contact_number: String,
        val contact_person: String,
        val country_code: Int,
        val country_id: Int,
        val cui_selectdata: List<Int>,
        val cuisine_data: List<CuisineData>,
        val currency: Any,
        val currency_symbol: String,
        val description: String,
        val device_id: Any,
        val device_token: String,
        val device_type: String,
        val email: String,
        val estimated_delivery_time: String,
        val free_delivery: Int,
        val id: Int,
        val is_bankdetail: Int,
        val is_veg: String,
        val language: String,
        val latitude: Double,
        val longitude: Double,
        val offer_min_amount: String,
        val offer_percent: Double,
        val otp: Any,
        val password: String,
        val picture: String,
        val rating: Any,
        val status: Int,
        val store_gst: Int,
        val store_location: String,
        val flat_no: String,
        val street: String,
        val store_name: String,
        val store_packing_charges: Double,
        val store_response_time: Int,
        val store_type_id: Int,
        val store_zipcode: String,
        val time_data: List<TimeData>,
        val wallet_balance: String,
        val zone_data: List<ZoneData>,
        val zone_id: Int
    ) {
        data class CityData(
            val city: City,
            val city_id: Int,
            val company_id: Int,
            val country_id: Int,
            val id: Int,
            val state_id: Int,
            val status: Int
        ) {
            data class City(
                val city_name: String,
                val country_id: Int,
                val id: Int,
                val state_id: Int,
                val status: Any
            )
        }

        data class CuisineData(
            val id: Int,
            val name: String,
            val status: Int,
            val store_type_id: Int
        )

        data class TimeData(
            val id: Int,
            val store_day: String,
            val store_end_time: String,
            val store_id: Int,
            val store_start_time: String
        )

        data class ZoneData(
            val city_id: Int,
            val company_id: Int,
            val id: Int,
            val name: String,
            val status: String,
            val user_type: String
        )
    }
}