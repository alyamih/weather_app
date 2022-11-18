package com.example.weather_app.ui.presentation

import com.example.weather_app.model.Weather

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: List<Weather> = listOf(),
    val currentDay: Int = 0,
    val dialogShown: Boolean = false,
    val error: String = ""
) {
    val currentWeather
        get() = weather.getOrNull(currentDay) ?: Weather()
}