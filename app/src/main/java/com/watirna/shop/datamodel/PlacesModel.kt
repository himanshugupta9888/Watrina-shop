package com.watirna.shop.datamodel

import android.os.Parcel
import android.os.Parcelable

data class PlacesModel(var mPlaceId: String, var mPrimary: String, var mSecondary: String, var mFullAddress: String) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mPlaceId)
        parcel.writeString(mPrimary)
        parcel.writeString(mSecondary)
        parcel.writeString(mFullAddress)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlacesModel> {
        override fun createFromParcel(parcel: Parcel): PlacesModel {
            return PlacesModel(parcel)
        }

        override fun newArray(size: Int): Array<PlacesModel?> {
            return arrayOfNulls(size)
        }
    }

}