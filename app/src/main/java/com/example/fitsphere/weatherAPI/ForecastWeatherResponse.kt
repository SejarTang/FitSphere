package com.example.fitsphere.weatherAPI

import com.google.gson.annotations.SerializedName

data class ForecastWeatherResponse(
    @SerializedName("list") val list: List<ForecastItem>,
    @SerializedName("city") val city: CityInfo
)

data class ForecastItem(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: MainInfo,
    @SerializedName("weather") val weather: List<WeatherInfo>,
    @SerializedName("wind") val wind: WindInfo,
    @SerializedName("dt_txt") val dtTxt: String
)

data class MainInfo(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val humidity: Int
)

data class WeatherInfo(
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class WindInfo(
    @SerializedName("speed") val speed: Double
)

data class CityInfo(
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String
)

