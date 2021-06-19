package com.watirna.shop.datamodel

data class HistoryDataModel(
    var error: List<Any?>? = listOf(),
    var message: String? = "",
    var responseData: ResponseData? = ResponseData(),
    var statusCode: String? = "", // 200
    var title: String? = "" // OK
) {
    data class ResponseData(
        var current_page: Int? = 0, // 1
        var data: ArrayList<Data> = arrayListOf<Data>(),
        var first_page_url: String? = "", // https://api.gox.network/api/v1/shop/shoprequesthistory?page=1
        var from: Int? = 0, // 1
        var last_page: Int? = 0, // 1
        var last_page_url: String? = "", // https://api.gox.network/api/v1/shop/shoprequesthistory?page=1
        var next_page_url: Any? = Any(), // null
        var path: String? = "", // https://api.gox.network/api/v1/shop/shoprequesthistory
        var per_page: String? = "", // 10
        var prev_page_url: Any? = Any(), // null
        var to: Int? = 0, // 4
        var total: Int? = 0 // 4
    ) {
        data class Data(
            var admin_service: String? = "", // ORDER
            var assigned_at: String? = "", // 2020-03-03 14:33:02
            var assigned_time: String? = "", // 03-03-2020 20:03:02
            var cancel_reason: Any? = Any(), // null
            var cancelled_by: Any? = Any(), // null
            var city_id: Int? = 0, // 3659
            var country_id: Int? = 0, // 101
            var created_at: String? = "", // 2020-03-03 14:30:53
            var created_time: String? = "", // 03-03-2020 20:00:53
            var currency: String? = "", // ₹
            var delivery: Delivery? = Delivery(),
            var delivery_address: String? = "", // {"id":1,"latitude":13.0585315,"longitude":80.2538611,"map_address":"104\/29, Thousand Lights West, Thousand Lights, Chennai, Tamil Nadu 600006, India","flat_no":"5","street":"gnm"}
            var delivery_date: Any? = Any(), // null
            var description: String? = "",
            var discount: String? = "", // 10.00
            var id: Int? = 0, // 6
            var note: Any? = Any(), // null
            var order_otp: String? = "", // 1660
            var order_ready_status: Int? = 0, // 0
            var order_ready_time: Int? = 0, // 1
            var order_type: String? = "", // DELIVERY
            var paid: Int? = 0, // 1
            var payment_mode: String? = "", // CASH
            var pickup: Pickup? = Pickup(),
            var pickup_address: String? = "", // {"id":2,"picture":"https:\/\/api.gox.network\/images\/order\/stores\/kfc_demo.png","contact_number":"2","store_type_id":1,"latitude":13.0569467,"longitude":80.24246900000003,"store_location":"Nungambakkam, Chennai, Tamil Nadu, India","store_name":"KFC Demo","currency_symbol":"$","storetype":{"id":1,"name":"Foodie","category":"FOOD","status":1}}
            var promocode_id: Int? = 0, // 0
            var provider: Provider? = Provider(),
            var provider_id: Int? = 0, // 31
            var provider_rated: Int? = 0, // 1
            var provider_vehicle_id: Any? = Any(), // null
            var request_type: String? = "", // AUTO
            var route_key: String? = "", // mnunA}qyhNCIASGGAGQ@]CuF_@{BKaBEiB@yBDqAHY@WAa@Ig@lBGLgCvBiChBa@^MPaBfEa@x@ANQvAGxAe@fCm@jCKn@K|BhFJ`HR`AH`CB|C@RBFBz@`Ah@l@@P{@j@WXW^W`AY~BMd@MTYXyAr@c@XSPa@j@a@`AW`AAVBLX@h@FhGn@`Dd@~A\nBj@pAXVBRANI`CWdC_@|@EjA@d@D\HB?D?l@Lp@RZNXJv@Pv@Hz@Et@Kn@E@RgAJyAJu@Ii@MYO]Ms@Wo@CQDA?KR
            var schedule_datetime: Any? = Any(), // null
            var schedule_status: Int? = 0, // 0
            var status: String? = "", // COMPLETED
            var store_id: Int? = 0, // 2
            var store_order_invoice_id: String? = "", // ORD1583245853647
            var store_type_id: Int? = 0, // 1
            var timezone: String? = "", // Asia/Kolkata
            var total_amount: String? = "", // 270.00
            var user: User? = User(),
            var user_address_id: Int? = 0, // 1
            var user_id: Int? = 0, // 31
            var user_rated:Double? =0.0 // 1
        ) {
            data class Delivery(
                var flat_no: String? = "", // 5
                var id: Int? = 0, // 1
                var latitude: Double? = 0.0, // 13.0585315
                var longitude: Double? = 0.0, // 80.2538611
                var map_address: String? = "", // 104/29, Thousand Lights West, Thousand Lights, Chennai, Tamil Nadu 600006, India
                var street: String? = "" // gnm
            )

            data class Pickup(
                var contact_number: String? = "", // 2
                var currency_symbol: String? = "", // $
                var id: Int? = 0, // 2
                var latitude: Double? = 0.0, // 13.0569467
                var longitude: Double? = 0.0, // 80.24246900000003
                var picture: String? = "", // https://api.gox.network/images/order/stores/kfc_demo.png
                var store_location: String? = "", // Nungambakkam, Chennai, Tamil Nadu, India
                var store_name: String? = "", // KFC Demo
                var store_type_id: Int? = 0, // 1
                var storetype: Storetype? = Storetype()
            ) {
                data class Storetype(
                    var category: String? = "", // FOOD
                    var id: Int? = 0, // 1
                    var name: String? = "", // Foodie
                    var status: Int? = 0 // 1
                )
            }

            data class Provider(
                var activation_status: Int? = 0, // 1
                var admin_id: Any? = Any(), // null
                var city_id: Int? = 0, // 3659
                var country_code: String? = "", // 91
                var country_id: Int? = 0, // 101
                var currency: Any? = Any(), // null
                var currency_symbol: String? = "", // ₹
                var current_location: String? = "", // Thousand Lights, Chennai, Tamil Nadu, India
                var current_ride_vehicle: Any? = Any(), // null
                var current_store: Any? = Any(), // null
                var device_id: Any? = Any(), // null
                var device_token: Any? = Any(), // null
                var device_type: Any? = Any(), // null
                var email: String? = "", // wrogntest@gmail.com
                var first_name: String? = "", // RV
                var gender: String? = "", // MALE
                var id: Int? = 0, // 31
                var is_assigned: Int? = 0, // 0
                var is_bankdetail: Int? = 0, // 1
                var is_document: Int? = 0, // 1
                var is_online: Int? = 0, // 1
                var is_service: Int? = 0, // 1
                var language: String? = "", // en
                var last_name: String? = "", // Provider
                var latitude: Double? = 0.0, // 13.0616905
                var login_by: String? = "", // GOOGLE
                var longitude: Double? = 0.0, // 80.2544454
                var mobile: String? = "", // 7410852096
                var otp: Any? = Any(), // null
                var payment_gateway_id: Any? = Any(), // null
                var payment_mode: String? = "", // CASH
                var picture: String? = "", // https://api.gox.network/storage/1/provider/profile/106250159970435606272.jpg
                var qrcode_url: String? = "", // https://api.gox.network/storage/1/qr_code/31.png
                var rating: Double? = 0.0, // 5
                var referal_count: Int? = 0, // 0
                var referral_unique_id: String? = "", // 419A9A
                var social_unique_id: String? = "", // 106250159970435606272
                var state_id: Int? = 0, // 2144
                var status: String? = "", // APPROVED
                var stripe_cust_id: Any? = Any(), // null
                var unique_id: String? = "", // PRV31
                var wallet_balance: Double? = 0.0, // 200
                var zone_id: Int? = 0 // 2
            )

            data class User(
                var city_id: Int? = 0, // 3659
                var company_id: Int? = 0, // 1
                var country_code: String? = "", // 91
                var country_id: Int? = 0, // 101
                var created_at: String? = "", // 2020-03-03 09:52:48
                var currency_symbol: String? = "", // ₹
                var email: String? = "", // wrogntest@gmail.com
                var first_name: String? = "", // Ravi
                var gender: String? = "", // MALE
                var id: Int? = 0, // 31
                var language: String? = "", // en
                var last_name: String? = "", // User
                var latitude: Any? = Any(), // null
                var login_by: String? = "", // GOOGLE
                var longitude: Any? = Any(), // null
                var mobile: String? = "", // 7410852096
                var payment_mode: String? = "", // CASH
                var picture: String? = "", // https://api.gox.network/storage/1/user/5a6237a0578b21ccf01f12ea189daad51092b7fb.jpg
                var rating: Double? = 0.0, // 5
                var referral_unique_id: String? = "", // B47D20
                var state_id: Int? = 0, // 2144
                var status: Int? = 0, // 1
                var unique_id: String? = "", // USR31
                var user_type: String? = "", // NORMAL
                var wallet_balance: Double? = 0.0// 0
            )
        }
    }
}