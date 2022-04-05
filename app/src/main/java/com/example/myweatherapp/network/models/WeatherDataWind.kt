package com.example.myweatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
class WeatherDataWind (
    val speed: Float,

    val deg: Int,
)