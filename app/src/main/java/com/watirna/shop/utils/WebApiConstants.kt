package com.watirna.shop.utils

class WebApiConstants {

    object AUTHKEY {
        val SALT_KEY = "salt_key"
    }

    object LoginConstants {
        val EMAIL = "email"
        val PASSWORD = "password"
        val DEVICETYPE = "device_type"
        val DEVICETOKEN = "device_token"
    }

    object OrderAccept {
        val STOREID="store_order_id"
        val COOKTIME="cooking_time"
        val DELIVERYDATE="delivery_date"
        val USERID="user_id"
        val SHOPID="store_id"
        val   ID="id"
    }

    object ChangePassword{
        val OLD_PASSWORD="old_password"
        val NEW_PASSWORD="password"
        val CONFIRM_PASSWORD="password_confirmation"
    }
    object Profile{
        val USERNAME ="store_name"
        val COUNTRYCODE ="country_code"
        val PHONENUMBER ="contact_number"
        val EMAIL ="email"
        val CITY   ="city_name"
        val PROFILEIMAGE ="picture"
    }

    object EditRestaurant{
        val COUNTRY_ID="country_id"
        val CITY_ID="city_id"
        val STORE_ZIPCODE="store_zipcode"
        val ZONE_ID="zone_id"
        val LONGITUDE="longitude"
        val LATITUDE="latitude"
        val _METHOD="_method"
        val SHOPNAME ="store_name"
        val COUNTRYCODE ="country_code"
        val PHONENUMBER ="contact_number"
        val EMAIL ="email"
        val CITY   ="city_name"
        val DISHIMAGE ="picture"
        val CUISINE ="cui_selectdata"
        val CUISINE_ID="cuisine_id"
        val STORE_GST ="store_gst"
        val COMMISSION ="commission"
        val STORE_PACKAGING_CHARGES ="store_packing_charges"
        val OFFER_MIN_AMOUNT ="offer_min_amount"
        val OFFER_PERCENT ="offer_percent"
        val ESTIMATED_DELIVERY_TIME ="estimated_delivery_time"
        val DESCRIPTION ="description"
        val STREET ="street"
        val FLATNO ="flat_no"
        val LANDMARK ="store_location"
        val IS_PURE_VEG ="is_veg"
        val STATUS ="status"
        val STORE_TYPE_ID="store_type_id"
        val CONTACT_PERSON="contact_person"
        val STORE_RESPONSE_TIME="store_response_time"

    }

}