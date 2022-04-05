package com.example.myweatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
class WeatherDataCoordinates (
    val lon: Float,

    val lat: Float,
)