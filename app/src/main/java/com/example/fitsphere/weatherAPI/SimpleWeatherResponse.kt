package com.example.fitsphere.weatherAPI
data class SimpleWeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val timezone: Int
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int
)

data class Weather(
    val main: String,
    val description: String
)

data class Wind(
    val speed: Double
)
