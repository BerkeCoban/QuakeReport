package com.quake.report.data

import com.quake.report.data.model.CountResponse
import com.quake.report.data.model.QuakeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface QuakeApi {

    @GET("query")
    suspend fun getByDates(
        @Query("format") format: String = "geojson",
        @Query("starttime") startTime: String,
        @Query("endtime") endTime: String): QuakeResponse

    @GET("query")
    suspend fun getByDatesAndMagnitude(
        @Query("format") format: String = "geojson",
        @Query("starttime") startTime: String,
        @Query("endtime") endTime: String,
        @Query("minmagnitude") minMagnitude: String): QuakeResponse

    @GET("query")
    suspend fun getStartDateAndMagnitude(
        @Query("format") format: String = "geojson",
        @Query("starttime") startTime: String,
        @Query("minmagnitude") minMagnitude: String): QuakeResponse

    @GET("count")
    suspend fun getCountsWithDates(
        @Query("format") format: String = "geojson",
        @Query("starttime") startTime: String,
        @Query("endtime") endTime: String): CountResponse

    @GET("count")
    suspend fun getCountsWithDatesAndMagnitude(
        @Query("format") format: String = "geojson",
        @Query("starttime") startTime: String,
        @Query("endtime") endTime: String,
        @Query("minmagnitude") minMagnitude: String): CountResponse

    @GET("count")
    suspend fun getCountsWithStartDateAndMagnitude(
        @Query("format") format: String = "geojson",
        @Query("starttime") startTime: String,
        @Query("minmagnitude") minMagnitude: String): CountResponse

}