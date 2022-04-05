package com.example.myweatherapp.network.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    val coord: WeatherDataCoordinates,

    val weather: Array<WeatherDataWeather>,

    val main: WeatherDataMain,

    val visibility: Int,

    val wind: WeatherDataWind,

    val clouds: WeatherDataClouds,

    val dt: Long,

    val sys: WeatherDataSun,

    val timezone: Int,

    val id: Int,

    val name: String,
)