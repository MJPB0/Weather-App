package com.example.myweatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
class WeatherDataWeather  (
    val id: Int,

    val main: String,

    val description: String,

    val icon: String,
)