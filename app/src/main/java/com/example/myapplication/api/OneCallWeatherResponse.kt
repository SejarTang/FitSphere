data class OneCallWeatherResponse(
    val current: Current
)

data class Current(
    val temp: Double,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<Weather>
)

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)
