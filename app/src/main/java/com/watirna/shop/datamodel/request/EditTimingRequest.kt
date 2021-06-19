package com.watirna.shop.datamodel.request

import com.google.gson.annotations.SerializedName

data class EditTimingRequest(
    @SerializedName("_method")
    private val method: String = "PATCH",
    @SerializedName("day")
    private val dayList: List<String>,
    @SerializedName("hours_opening")
    private val openingHoursList: HashMap<String, String>,
    @SerializedName("hours_closing")
    private val closingHoursList: HashMap<String, String>
)