package com.quake.report.data.model

import com.google.gson.annotations.SerializedName

data class CountResponse(
    @SerializedName("count") var count: Int? = null,
    @SerializedName("maxAllowed") var maxAllowed: Int? = null
)