package com.example.fitsphere.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitsphere.util.TipUtil
import com.example.fitsphere.weatherAPI.ForecastWeatherResponse
import com.example.fitsphere.weatherAPI.SimpleWeatherResponse
import com.example.fitsphere.weatherAPI.WeatherApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _tip = MutableStateFlow(TipUtil.generateTip())
    val tip: StateFlow<String> = _tip

    fun refreshTip() {
        var newTip: String
        do {
            newTip = TipUtil.generateTip()
        } while (newTip == _tip.value)
        _tip.value = newTip
    }

    private val _weather = MutableStateFlow<SimpleWeatherResponse?>(null)
    val weather: StateFlow<SimpleWeatherResponse?> = _weather

    private val _forecast = MutableStateFlow< ForecastWeatherResponse?>(null)
    val forecast: StateFlow<ForecastWeatherResponse?> = _forecast

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val apiKey = "5b9fe71dc1675f8a3c71d874470ed3c2"
                val service = WeatherApiService.create()

                val current = service.getCurrentWeather(lat, lon, apiKey)
                val forecast = service.getForecastWeather(lat, lon, apiKey)

                _weather.value = current
                _forecast.value = forecast

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
