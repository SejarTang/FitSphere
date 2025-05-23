package com.example.myapplication.viewmodel

import OneCallWeatherResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ViewWeatherModel : ViewModel() {
    private val _weatherData = MutableStateFlow<OneCallWeatherResponse?>(null)
    val weatherData: StateFlow<OneCallWeatherResponse?> = _weatherData

    private val _hourlyData = MutableStateFlow<List<HourlyWeather>>(emptyList())
    val hourlyData: StateFlow<List<HourlyWeather>> = _hourlyData

    private val apiKey = "5b9fe71dc1675f8a3c71d874470ed3c2"

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val current = WeatherApiService.create().getCurrentWeather(
                    lat = lat,
                    lon = lon,
                    apiKey = apiKey
                )
                val hourly = WeatherApiService.create().getHourlyForecast(
                    lat = lat,
                    lon = lon,
                    apiKey = apiKey
                )

                _weatherData.value = current

                // 解析前6小时数据
                _hourlyData.value = hourly.list.take(6).map {
                    HourlyWeather(
                        time = unixToHour(it.dt),
                        temperature = "${it.main.temp.toInt()}°C",
                        condition = it.weather.firstOrNull()?.description ?: "",
                        icon = iconToEmoji(it.weather.firstOrNull()?.icon)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _weatherData.value = null
                _hourlyData.value = emptyList()
            }
        }
    }

    private fun unixToHour(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val format = SimpleDateFormat("ha", Locale.getDefault())
        return format.format(date)
    }

    private fun iconToEmoji(icon: String?): String {
        return when (icon) {
            "01d", "01n" -> "☀️"
            "02d", "02n" -> "⛅"
            "03d", "03n" -> "☁️"
            "09d", "09n" -> "🌧"
            "10d", "10n" -> "🌦"
            "11d", "11n" -> "🌩"
            "13d", "13n" -> "❄️"
            "50d", "50n" -> "🌫"
            else -> "☁️"
        }
    }
}
