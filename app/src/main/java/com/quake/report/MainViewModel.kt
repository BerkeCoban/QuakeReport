package com.quake.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quake.report.data.ResponseMapper
import com.quake.report.data.RetrofitInstance
import com.quake.report.data.model.QuakeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _emptyData = MutableStateFlow(false)
    val emptyData: StateFlow<Boolean> = _emptyData

    private val _progressDialog = MutableStateFlow(false)
    val progressDialog: StateFlow<Boolean> = _progressDialog

    fun updateEmptyData(data: Boolean) {
        _emptyData.value = data
    }

    fun updateProgressValue(data: Boolean) {
        _progressDialog.value = data
    }


    fun getSplashData(startTime: String, minMagnitude: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getStartDateAndMagnitude(
                    startTime = startTime,
                    minMagnitude = minMagnitude
                )
                if (response.features.isNotEmpty()) {
                    MainActivity.splashData = ResponseMapper.map(response)
                    _splashData.postValue(response)
                } else {
                    _emptyData.value = true
                }
            } catch (e: Exception) {
                // TODO: phase 2 use different dialog
                _emptyData.value = true
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
                _emptyData.value = true
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
                _emptyData.value = true
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
                _emptyData.value = true
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
                _emptyData.value = true
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

                } else {
                    _emptyData.value = true
                }
            } catch (e: Exception) {
                _emptyData.value = true
            }
        }
    }
}