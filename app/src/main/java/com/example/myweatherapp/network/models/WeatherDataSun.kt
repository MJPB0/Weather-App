package com.example.myweatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
class WeatherDataSun (
    val country: String,

    val sunrise: Long,

    val sunset: Long,
)