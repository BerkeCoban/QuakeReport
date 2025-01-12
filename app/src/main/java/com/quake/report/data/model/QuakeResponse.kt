package com.quake.report.data.model

import com.google.gson.annotations.SerializedName

data class QuakeResponse(
    @SerializedName("type") var type: String? = null,
    @SerializedName("metadata") var metadata: Metadata? = Metadata(),
    @SerializedName("features") var features: ArrayList<Features> = arrayListOf(),
)