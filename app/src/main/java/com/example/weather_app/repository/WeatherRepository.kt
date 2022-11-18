package com.example.weather_app.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weather_app.model.LocalWeatherInfo
import com.example.weather_app.model.Location
import com.example.weather_app.model.Weather
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class WeatherRepository(
    private val context: Context
) {

    suspend fun getLocalWeather(location: Location): LocalWeatherInfo {
        val city = getCity(location.lat, location.lon)
        return getWeatherForCity(city)
    }

    suspend fun getWeatherForCity(city: String) =
        suspendCoroutine<LocalWeatherInfo> { continuation ->
            val queue = Volley.newRequestQueue(context)

            val url = "https://api.weatherapi.com/v1/forecast.json?" +
                    "key=$API_KEY" +
                    "&q=$city" +
                    "&days=8" +
                    "&aqi=no&alerts=no"

            queue.add(
                StringRequest(
                    Request.Method.GET,
                    url,
                    { response ->
                        val list = getWeatherByDays(response)
                        continuation.resume(LocalWeatherInfo(city, list))
                    },
                    continuation::resumeWithException
                )
            )
        }


    private fun getWeatherByDays(response: String): List<Weather> {
        if (response.isEmpty()) return listOf()
        val list = ArrayList<Weather>()
        val mainObject = JSONObject(response)
        val city = mainObject.getJSONObject("location").getString("name")
        val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        for (i in 0 until days.length()) {
            val item = days[i] as JSONObject
            list.add(
                Weather(
                    city,
                    item.getString("date"),
                    "",
                    item.getJSONObject("day").getJSONObject("condition").getString("text"),
                    item.getJSONObject("day").getJSONObject("condition").getString("icon"),
                    item.getJSONObject("day").getString("maxtemp_c"),
                    item.getJSONObject("day").getString("mintemp_c"),
                    item.getJSONArray("hour").toString()
                )
            )
        }
        list[0] = list[0].copy(
            time = mainObject.getJSONObject("current").getString("last_updated"),
            currentTemp = mainObject.getJSONObject("current").getString("temp_c")
        )
        return list
    }

    private suspend fun getCity(
        lat: Double,
        lon: Double
    ) = suspendCoroutine<String> { continuation ->
        val queue = Volley.newRequestQueue(context)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$APP_ID"
        queue.add(
            StringRequest(
                Request.Method.GET,
                url,
                { response -> continuation.resume(JSONObject(response).getString("name")) },
                continuation::resumeWithException
            )
        )
    }

    companion object {
        private const val API_KEY = "33e481175ad6436eb6c141914221411"
        private const val APP_ID = "a9ac1745bc8669b6800d1f8c1b49063f"
    }
}


