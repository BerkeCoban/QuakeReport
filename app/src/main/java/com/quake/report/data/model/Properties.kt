package com.quake.report.data.model

import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("mag") var mag: Double? = null,
    @SerializedName("place") var place: String? = null,
    @SerializedName("time") var time: Double? = null,
    @SerializedName("updated") var updated: Double? = null,
    @SerializedName("tz") var tz: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("detail") var detail: String? = null,
    @SerializedName("felt") var felt: String? = null,
    @SerializedName("cdi") var cdi: String? = null,
    @SerializedName("mmi") var mmi: String? = null,
    @SerializedName("alert") var alert: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("tsunami") var tsunami: Int? = null,
    @SerializedName("sig") var sig: Int? = null,
    @SerializedName("net") var net: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("ids") var ids: String? = null,
    @SerializedName("sources") var sources: String? = null,
    @SerializedName("types") var types: String? = null,
    @SerializedName("nst") var nst: Int? = null,
    @SerializedName("dmin") var dmin: Double? = null,
    @SerializedName("rms") var rms: Double? = null,
    @SerializedName("gap") var gap: Double? = null,
    @SerializedName("magType") var magType: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("title") var title: String? = null
)