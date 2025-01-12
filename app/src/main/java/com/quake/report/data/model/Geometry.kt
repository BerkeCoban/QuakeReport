package com.quake.report.data.model

import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("type") var type: String? = null,
    @SerializedName("coordinates") var coordinates: ArrayList<Double> = arrayListOf()
)