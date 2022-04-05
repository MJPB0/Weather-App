package com.example.myweatherapp.network.service

import android.util.Log
import com.example.myweatherapp.network.models.WeatherData
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class ApiService {
    private val cache: MutableMap<String, WeatherData> = mutableMapOf()
    private val appId: String = "7509fc241c8574b3089c12031b4449ed"

    private val client = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private fun getUrl(query: String): String {
        return "https://api.openweathermap.org/data/2.5/weather?q=${query},pl&APPID=${this.appId}&lang=pl&units=metric"
    }

    suspend fun getRawData(query: String): WeatherData? {
        if (this.cache.containsKey(query)) {
            return this.cache[query]!!
        }

        try {
            val data: WeatherData = client.request {
                url(getUrl(query))

                method = HttpMethod.Get
            }

            this.cache[query] = data
            this.cache[data.name] = data

            return data
        } catch (t: Throwable) {
            t.message?.let { Log.d("API", it) }
        }
        return null
    }
}