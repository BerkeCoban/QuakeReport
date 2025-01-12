package com.quake.report.data

import com.quake.report.data.model.QuakeResponse
import com.quake.report.data.model.UiResponse

object ResponseMapper {
    fun map(data: QuakeResponse): ArrayList<UiResponse> {
        return arrayListOf<UiResponse>().apply {
            data.features.forEach { feature ->
                add(
                    UiResponse(
                        latitude = feature.geometry?.coordinates?.get(1),
                        longitude = feature.geometry?.coordinates?.get(0),
                        place = feature.properties?.place,
                        magnitude = feature.properties?.mag,
                        time = feature.properties?.time,
                        lastUpdated = feature.properties?.updated,
                        detailUrl = feature.properties?.url,
                        title = feature.properties?.title
                    )
                )
            }
        }
    }
}
