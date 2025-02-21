package com.quake.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quake.report.MainActivity.Companion
import com.quake.report.data.ResponseMapper
import com.quake.report.data.RetrofitInstance
import com.quake.report.data.model.QuakeResponse
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val apiService = RetrofitInstance.api

    private val _splashData = MutableLiveData<QuakeResponse>()
    val splashData: LiveData<QuakeResponse> = _splashData

    private val _countData = MutableLiveData<Int>()
    val countData: LiveData<Int> = _countData

    private val _secondCountData = MutableLiveData<Int>()
    val secondCountData: LiveData<Int> = _secondCountData

    private val _monthlyCountData = MutableLiveData<Int>()
    val monthlyCountData: LiveData<Int> = _monthlyCountData


    fun getSplashData(startTime: String, minMagnitude: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getStartDateAndMagnitude(
                    startTime = startTime,
                    minMagnitude = minMagnitude
                )
                if (response.features.isNotEmpty()) {
                    _splashData.postValue(response)
                }
            } catch (e: Exception) {
                Log.d("error: ", e.toString())
            }
        }
    }

    fun getCountData(startTime: String, endTime: String) {
        viewModelScope.launch {
            try {
                val response =
                    apiService.getCountsWithDates(startTime = startTime, endTime = endTime)
                _countData.postValue(response.count!!)
            } catch (e: Exception) {
                Log.d("error: ", e.toString())
            }
        }
    }

    fun getSecondCountData(startTime: String, endTime: String) {
        viewModelScope.launch {
            try {
                val response =
                    apiService.getCountsWithDates(startTime = startTime, endTime = endTime)
                _secondCountData.postValue(response.count!!)
            } catch (e: Exception) {
                Log.d("error: ", e.toString())
            }
        }
    }

    fun getMonthlyCountData(startTime: String, endTime: String) {
        viewModelScope.launch {
            try {
                val response =
                    apiService.getCountsWithDates(startTime = startTime, endTime = endTime)
                _monthlyCountData.postValue(response.count!!)
            } catch (e: Exception) {
                Log.d("error: ", e.toString())
            }
        }
    }

    fun getCountDataToday(startTime: String, minMagnitude: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getCountsWithStartDateAndMagnitude(
                    startTime = startTime,
                    minMagnitude = minMagnitude
                )
                _countData.postValue(response.count!!)
            } catch (e: Exception) {
                Log.d("error: ", e.toString())
            }
        }
    }

    fun getFilteredHomeData(startTime: String, endTime: String, minMagnitude: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getByDatesAndMagnitude(
                    startTime = startTime,
                    endTime = endTime,
                    minMagnitude = minMagnitude
                )
                if (response.features.isNotEmpty()) {
                    MainActivity.splashData = ResponseMapper.map(response)
                    _splashData.postValue(response)

                }
            } catch (e: Exception) {
                Log.d("error: ", e.toString())
            }
        }
    }
}