package com.example.myweatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDataMain (
    val temp: Float,

    val feels_like: Float,

    val temp_min: Float,

    val temp_max: Float,

    val pressure: Int,

    val humidity: Int,
)