package com.example.weather_app.model

data class Weather(
    val city: String = "",
    val time: String = "",
    val currentTemp: String = "",
    val condition: String = "",
    val icon: String = "",
    val max_temp: String = "",
    val min_temp: String = "",
    val hours: String = ""
)
