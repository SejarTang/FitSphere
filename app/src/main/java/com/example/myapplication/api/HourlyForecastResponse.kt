package com.example.myapplication.api


import com.google.gson.annotations.SerializedName

data class HourlyForecastResponse(
    @SerializedName("list")
    val list: List<ForecastData>
)

data class ForecastData(
    @SerializedName("dt")
    val dt: Long, // Unix timestamp

    @SerializedName("main")
    val main: MainData,

    @SerializedName("weather")
    val weather: List<WeatherDescription>
)

data class MainData(
    @SerializedName("temp")
    val temp: Double
)

data class WeatherDescription(
    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String
)

data class HourlyWeather(
    val time: String,
    val temperature: String,
    val condition: String,
    val icon: String
)