package com.quake.report.data.model

import com.google.gson.annotations.SerializedName

data class Features(
    @SerializedName("type") var type: String? = null,
    @SerializedName("properties") var properties: Properties? = Properties(),
    @SerializedName("geometry") var geometry: Geometry? = Geometry(),
    @SerializedName("id") var id: String? = null
)