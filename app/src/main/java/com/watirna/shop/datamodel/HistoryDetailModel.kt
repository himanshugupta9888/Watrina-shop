package com.watirna.shop.datamodel

data class HistoryDetailModel(
    var error: List<Any?>? = listOf(),
    var message: String? = "",
    var responseData: ResponseData? = ResponseData(),
    var statusCode: String? = "",
    var title: String? = ""
) {
    data class ResponseData(
        var admin_service: String? = "",
        var assigned_at: String? = "",
        var assigned_time: String? = "",
        var cancel_reason: Any? = Any(),
        var cancelled_by: String? = "",
        var city_id: Int? = 0,
        var country_id: Int? = 0,
        var created_at: String? = "",
        var created_time: String? = "",
        var currency: String? = "",
        var delivery: Delivery? = Delivery(),
        var delivery_address: String? = "",
        var delivery_date: Any? = Any(),
        var description: String? = "",
        var id: Int? = 0,
        var leave_at_door: Int? = 0,
        var note: Any? = Any(),
        var order_invoice: OrderInvoice? = OrderInvoice(),
        var order_otp: String? = "",
        var order_ready_status: Int? = 0,
        var order_ready_time: Int? = 0,
        var order_type: String? = "",
        var paid: Int? = 0,
        var pickup: Pickup? = Pickup(),
        var pickup_address: String? = "",
        var promocode_id: Int? = 0,
        var provider: Any? = Any(),
        var provider_id: Any? = Any(),
        var provider_rated: Double? = 0.0,
        var provider_vehicle_id: Any? = Any(),
        var request_type: String? = "",
        var route_key: String? = "",
        var schedule_datetime: Any? = Any(),
        var schedule_status: Int? = 0,
        var status: String? = "",
        var store_id: Int? = 0,
        var store_order_invoice_id: String? = "",
        var store_type_id: Int? = 0,
        var timezone: String? = "",
        var user: User? = User(),
        var user_address_id: Int? = 0,
        var user_id: Int? = 0,
        var user_rated:Double? =0.0
    ) {
        data class Delivery(
            var flat_no: String? = "",
            var id: Int? = 0,
            var latitude: Double? = 0.0,
            var longitude: Double? = 0.0,
            var map_address: String? = "",
            var street: String? = ""
        )

        data class OrderInvoice(
            var cart_details: String? = "",
            var cash: Int? = 0,
            var commision_amount: Double? = 0.0,
            var commision_per: Int? = 0,
            var delivery_amount:Double? =0.0,
            var delivery_per: Int? = 0,
            var discount:Double? =0.0,
            var gross:Double? =0.0,
            var id: Int? = 0,
            var item_price:Double? =0.0,
            var items: List<Item> = listOf(),
            var net: Double? = 0.0,
            var payable:Double? = 0.0,
            var payment_id: String? = "",
            var payment_mode: String? = "",
            var promocode_amount: Int? = 0,
            var promocode_id: Int? = 0,
            var status: Int? = 0,
            var store_id: Int? = 0,
            var store_order_id: Int? = 0,
            var store_package_amount: Int? = 0,
            var tax_amount:Double? = 0.0,
            var tax_per:Double? = 0.0,
            var total_amount:Double? = 0.0,
            var wallet_amount:Double? =0.0
        ) {
            data class Item(
                var cartaddon: List<Cartaddon?>? = listOf(),
                var id: Int? = 0,
                var item_price:Double? = 0.0,
                var note: Any? = Any(),
                var product: Product? = Product(),
                var product_data: Any? = Any(),
                var quantity: Int? = 0,
                var store: Store? = Store(),
                var store_id: Int? = 0,
                var store_item_id: Int? = 0,
                var store_order_id: Any? = Any(),
                var tot_addon_price: Double? = 0.0,
                var total_item_price:Double? =0.0,
                var user_id: Int? = 0
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
                    var id: Int? = 0,
                    var is_veg: String? = "",
                    var item_discount:Double? = 0.0,
                    var item_discount_type: String? = "",
                    var item_name: String? = "",
                    var item_price:Double? = 0.0,
                    var itemsaddon: List<ItemsAddOn?>? = listOf(),
                    var picture: String? = ""
                ){
                    data class ItemsAddOn(
                        val addon_name: String? = "",
                        val addon_price: Double? = 0.0,
                        val id: Int? = 0,
                        val store_addon_id: Any? = Any(),
                        val store_cart_id: Int? = 0,
                        val store_cart_item_id: Int? = 0,
                        val store_item_addons_id: Int? = 0
                    )
                }

                data class Store(
                    var city_id: Int? = 0,
                    var commission:Double? = 0.0,
                    var currency_symbol: String? = "",
                    var free_delivery: Int? = 0,
                    var id: Int? = 0,
                    var latitude: Double? = 0.0,
                    var longitude: Double? = 0.0,
                    var offer_min_amount: String? = "",
                    var offer_percent: Int? = 0,
                    var picture: String? = "",
                    var rating: Double? = 0.0,
                    var store_cusinie: List<StoreCusinie?>? = listOf(),
                    var store_gst:Double? = 0.0,
                    var store_name: String? = "",
                    var store_packing_charges:Double? = 0.0,
                    var store_type_id: Int? = 0,
                    var storetype: Storetype? = Storetype()
                ) {
                    data class StoreCusinie(
                        var cuisine: Cuisine? = Cuisine(),
                        var cuisines_id: Int? = 0,
                        var id: Int? = 0,
                        var store_id: Int? = 0,
                        var store_type_id: Int? = 0
                    ) {
                        data class Cuisine(
                            var id: Int? = 0,
                            var name: String? = "",
                            var status: Int? = 0,
                            var store_type_id: Int? = 0
                        )
                    }

                    data class Storetype(
                        var category: String? = "",
                        var id: Int? = 0,
                        var name: String? = "",
                        var status: Int? = 0
                    )
                }
            }
        }

        data class Pickup(
            var contact_number: String? = "",
            var currency_symbol: String? = "",
            var id: Int? = 0,
            var latitude: Double? = 0.0,
            var longitude: Double? = 0.0,
            var picture: String? = "",
            var store_location: String? = "",
            var store_name: String? = "",
            var store_type_id: Int? = 0,
            var storetype: Storetype? = Storetype()
        ) {
            data class Storetype(
                var category: String? = "",
                var id: Int? = 0,
                var name: String? = "",
                var status: Int? = 0
            )
        }

        data class User(
            var city_id: Int? = 0,
            var company_id: Int? = 0,
            var country_code: String? = "",
            var country_id: Int? = 0,
            var created_at: String? = "",
            var currency_symbol: String? = "",
            var email: String? = "",
            var first_name: String? = "",
            var gender: String? = "",
            var id: Int? = 0,
            var language: String? = "",
            var last_name: String? = "",
            var latitude: Any? = Any(),
            var login_by: String? = "",
            var longitude: Any? = Any(),
            var mobile: String? = "",
            var payment_mode: String? = "",
            var picture: String? = "",
            var rating: Double? = 0.0,
            var referral_unique_id: String? = "",
            var state_id: Int? = 0,
            var status: Int? = 0,
            var unique_id: String? = "",
            var user_type: String? = "",
            var wallet_balance:Double? = 0.0
        )
    }
}