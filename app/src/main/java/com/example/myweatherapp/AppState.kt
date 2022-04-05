package com.example.myweatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.network.models.WeatherData

class AppState {
    companion object {
        private val _data = MutableLiveData<WeatherData>()
        val data: LiveData<WeatherData> = _data

        @JvmStatic
        fun updateData(data: WeatherData) {
            _data.value = data
        }
    }
}