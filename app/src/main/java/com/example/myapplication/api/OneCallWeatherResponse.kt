package com.example.myapplication.api

data class OneCallWeatherResponse(
    val current: Current
)

data class Current(
    val temp: Double,
    val humidity: Int,
    val wind_speed: Double
)
