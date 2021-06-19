package com.watirna.shop.datamodel.product

import android.os.Parcel
import android.os.Parcelable

data class EditProductModel(
    var statusCode: String,
    var title: String,
    var message: String,
    var responseData: ResponseData
)

data class ResponseData(
    var id: Int,
    var store_id: Int,
    var item_name: String = "",
    var item_description: String,
    var picture: String,
    var store_category_id: Int,
    var is_veg: String,
    var item_price: Double,
    var item_discount: Double,
    var item_discount_type: String,
    var item_order:Double,
    var is_addon: Int,
    var status: String,
    var quantity: String,
    var unit_id: Int,
    var itemsaddon: ArrayList<ItemsAddOn>
)

data class ItemsAddOn(
    var id: Int,
    var store_id: Int,
    var store_item_id: Int,
    var store_addon_id: Int,
    var price: Double
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(store_id)
        parcel.writeInt(store_item_id)
        parcel.writeInt(store_addon_id)
        parcel.writeDouble(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemsAddOn> {
        override fun createFromParcel(parcel: Parcel): ItemsAddOn {
            return ItemsAddOn(parcel)
        }

        override fun newArray(size: Int): Array<ItemsAddOn?> {
            return arrayOfNulls(size)
        }
    }
}

