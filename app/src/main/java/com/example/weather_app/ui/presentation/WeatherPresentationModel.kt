package com.example.weather_app.ui.presentation

import android.util.Log
import com.example.weather_app.model.Location
import com.example.weather_app.repository.LocationRepository
import com.example.weather_app.repository.WeatherRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherPresentationModel(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) {
    private val pmScope = MainScope()

    private val _state: MutableStateFlow<WeatherState> = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    private fun changeState(copy: WeatherState.() -> WeatherState) {
        _state.value = copy(_state.value)
    }

    fun loadLocalWeather() = pmScope.launch {
        if (state.value.isLoading) return@launch
        withLoading {
            val location = locationRepository.getLocation() ?: Location(30.0, 30.0)
            val localWeather = try {
                weatherRepository.getLocalWeather(location)
            } catch (e: Throwable) {
                Log.e(TAG, "Smth went wrong", e)
                return@withLoading
            }
            changeState {
                copy(
                    weather = localWeather.weather,
                    currentDay = 0
                )
            }
        }
    }

    fun loadWeatherForCity(city: String) = pmScope.launch {
        if (state.value.isLoading) return@launch
        withLoading {
            val localWeather = try {
                weatherRepository.getWeatherForCity(city)
            } catch (e: Throwable) {
                Log.e(TAG, "Smth went wrong", e)
                return@withLoading
            }
            changeState {
                copy(
                    weather = localWeather.weather,
                    currentDay = 0
                )
            }
        }
    }

    fun toggleDialog(shown: Boolean) = changeState { copy(dialogShown = shown) }

    private suspend fun withLoading(
        function: suspend () -> Unit
    ) {
        changeState { copy(error = "", isLoading = true) }
        function()
        changeState { copy(isLoading = false) }
    }

    fun onDestroy() {
        pmScope.cancel()
    }

    companion object {
        private const val TAG = "WeatherPM"
    }
}