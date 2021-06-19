package com.watirna.shop.datamodel

data class NewOrderModel(
    val error: List<Any?>? = listOf(),
    val message: String? = "",
    val responseData: List<ResponseData?>? = listOf(),
    val statusCode: String? = "",
    val title: String? = ""
) {
    data class ResponseData(
        val admin_service: String? = "",
        val assigned_at: String? = "",
        val assigned_time: String? = "",
        val cancel_reason: Any? = Any(),
        val cancelled_by: Any? = Any(),
        val city_id: Int? = 0,
        val country_id: Int? = 0,
        val created_at: String? = "",
        val created_time: String? = "",
        val currency: String? = "",
        val delivery: Delivery? = Delivery(),
        val delivery_address: String? = "",
        val delivery_date: Any? = Any(),
        val description: String? = "",
        val id: Int? = 0,
        val invoice: Invoice? = Invoice(),
        val note: Any? = Any(),
        val order_otp: String? = "",
        val order_ready_status: Int? = 0,
        val order_ready_time: Any? = Any(),
        val order_type: String? = "",
        val paid: Int? = 0,
        val pickup: Pickup? = Pickup(),
        val pickup_address: String? = "",
        val promocode_id: Int? = 0,
        val provider: Any? = Any(),
        val provider_id: Any? = Any(),
        val provider_rated: Int? = 0,
        val provider_vehicle_id: Any? = Any(),
        val request_type: String? = "",
        val route_key: String? = "",
        val schedule_datetime: Any? = Any(),
        val schedule_status: Int? = 0,
        val status: String? = "",
        val store_id: Int? = 0,
        val store_order_dispute: Any? = Any(),
        val store_order_invoice_id: String? = "",
        val store_type_id: Int? = 0,
        val timezone: String? = "",
        val user: User? = User(),
        val user_address_id: Int? = 0,
        val user_id: Int? = 0,
        val user_rated: Int? = 0
    ) {
        data class Delivery(
            val flat_no: String? = "",
            val id: Int? = 0,
            val latitude: Double? = 0.0,
            val longitude: Double? = 0.0,
            val map_address: String? = "",
            val street: String? = ""
        )

        data class Invoice(
            val cart_details: String? = "",
            val cash: Int? = 0,
            val commision_amount: Double? = 0.0,
            val commision_per: Double? = 0.0,
            val delivery_amount: Double? = 0.0,
            val delivery_per: Int? = 0,
            val discount: Double? = 0.0,
            val gross: Double? = 0.0,
            val id: Int? = 0,
            val item_price: Double? = 0.0,
            val items: List<Item>? = listOf(),
            val net: Double? = 0.0,
            val payable: Double? = 0.0,
            val payment_id: String? = "",
            val payment_mode: String? = "",
            val promocode_amount: Double? = 0.0,
            val promocode_id: Int? = 0,
            val status: Int? = 0,
            val store_id: Int? = 0,
            val store_order_id: Int? = 0,
            val store_package_amount: Double? = 0.0,
            val tax_amount: Double? = 0.0,
            val tax_per: Double? = 0.0,
            val total_amount: Double? = 0.0,
            val wallet_amount: Double? = 0.0
        ) {
            data class Item(
                val cartaddon: List<Cartaddon?>? = listOf(),
                val id: Int? = 0,
                val item_price:Double? = 0.0,
                val note: Any? = Any(),
                val product: Product? = Product(),
                val product_data: Any? = Any(),
                val quantity: Int? = 0,
                val unit:String = "",
                val store: Store? = Store(),
                val store_id: Int? = 0,
                val store_item_id: Int? = 0,
                val store_order_id: Any? = Any(),
                val tot_addon_price: Double? = 0.0,
                val total_item_price: Double? = 0.0,
                val user_id: Int? = 0
            ) {
                data class Cartaddon(
                    val addon_name: String? = "",
                    val addon_price: Double? = 0.0,
                    val id: Int? = 0,
                    val store_addon_id: Any? = Any(),
                    val store_cart_id: Int? = 0,
                    val store_cart_item_id: Int? = 0,
                    val store_item_addons_id: Int? = 0
                )

                data class Product(
                    val id: Int? = 0,
                    val is_veg: String? = "",
                    val item_discount: Double? = 0.0,
                    val item_discount_type: String? = "",
                    val item_name: String? = "",
                    val item_price: Double? = 0.0,
                    val itemsaddon: List<Itemsaddon?>? = listOf(),
                    val picture: String? = ""
                ) {
                    data class Itemsaddon(
                        val addon_name: String? = "",
                        val id: Int? = 0,
                        val price: Double? = 0.0,
                        val store_addon_id: Int? = 0,
                        val store_id: Int? = 0,
                        val store_item_id: Int? = 0
                    )
                }

                data class Store(
                    val city_id: Int? = 0,
                    val commission: Double? = 0.0,
                    val currency_symbol: String? = "",
                    val free_delivery: Int? = 0,
                    val id: Int? = 0,
                    val latitude: Double? = 0.0,
                    val longitude: Double? = 0.0,
                    val offer_min_amount: String? = "",
                    val offer_percent: Double? = 0.0,
                    val picture: String? = "",
                    val rating: Any? = Any(),
                    val store_cusinie: List<StoreCusinie?>? = listOf(),
                    val store_gst: Int? = 0,
                    val store_name: String? = "",
                    val store_packing_charges: Int? = 0,
                    val store_type_id: Int? = 0,
                    val storetype: Storetype? = Storetype()
                ) {
                    data class StoreCusinie(
                        val cuisine: Cuisine? = Cuisine(),
                        val cuisines_id: Int? = 0,
                        val id: Int? = 0,
                        val store_id: Int? = 0,
                        val store_type_id: Int? = 0
                    ) {
                        data class Cuisine(
                            val id: Int? = 0,
                            val name: String? = "",
                            val status: Int? = 0,
                            val store_type_id: Int? = 0
                        )
                    }

                    data class Storetype(
                        val category: String? = "",
                        val id: Int? = 0,
                        val name: String? = "",
                        val status: Int? = 0
                    )
                }
            }
        }

        data class Pickup(
            val contact_number: String? = "",
            val currency_symbol: String? = "",
            val id: Int? = 0,
            val latitude: Double? = 0.0,
            val longitude: Double? = 0.0,
            val picture: String? = "",
            val store_location: String? = "",
            val store_name: String? = "",
            val store_type_id: Int? = 0,
            val storetype: Storetype? = Storetype()
        ) {
            data class Storetype(
                val category: String? = "",
                val id: Int? = 0,
                val name: String? = "",
                val status: Int? = 0
            )
        }

        data class User(
            val city_id: Int? = 0,
            val company_id: Int? = 0,
            val country_code: String? = "",
            val country_id: Int? = 0,
            val created_at: String? = "",
            val currency_symbol: String? = "",
            val email: String? = "",
            val first_name: String? = "",
            val gender: String? = "",
            val id: Int? = 0,
            val language: String? = "",
            val last_name: String? = "",
            val latitude: Any? = Any(),
            val login_by: String? = "",
            val longitude: Any? = Any(),
            val mobile: String? = "",
            val payment_mode: String? = "",
            val picture: String? = "",
            val rating: Double? = 0.0,
            val referral_unique_id: String? = "",
            val state_id: Int? = 0,
            val status: Int? = 0,
            val user_type: String? = "",
            val wallet_balance: Int? = 0
        )
    }
}