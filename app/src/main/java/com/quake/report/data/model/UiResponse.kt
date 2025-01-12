package com.quake.report.data.model


data class UiResponse(
    var latitude: Double? = null,
    var longitude: Double? = null,
    var place: String? = null,
    var magnitude: Double? = null,
    var time: Double? = null,
    var lastUpdated: Double? = null,
    var detailUrl: String? = null,
    var title: String? = null
)