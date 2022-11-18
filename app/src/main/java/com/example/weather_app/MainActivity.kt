package com.example.weather_app

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.example.weather_app.model.Weather
import com.example.weather_app.repository.LocationRepository
import com.example.weather_app.repository.WeatherRepository
import com.example.weather_app.ui.WeatherCard
import com.example.weather_app.ui.WeeklyWeatherList
import com.example.weather_app.ui.presentation.WeatherPresentationModel
import com.example.weather_app.ui.theme.Purple200
import com.example.weather_app.ui.theme.Weather_appTheme
import java.util.jar.Manifest


class MainActivity : ComponentActivity() {

    private val pm by lazy {
        WeatherPresentationModel(
            LocationRepository(this),
            WeatherRepository(this)
        )
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                pm.loadLocalWeather()
            } else {
                // TODO handle dialog
            }
        }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = pm.state.collectAsState().value

            WeatherApp(
                dialogShown = state.dialogShown,
                state.weather,
                state.currentWeather,
                {
                    pm.toggleDialog(false)
                    pm.loadWeatherForCity(it.trim())
                },
                { pm.toggleDialog(true) },
                { pm.toggleDialog(false) }
            )
        }

        val perm = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)

        if (perm == PERMISSION_GRANTED) {
            pm.loadLocalWeather()
        } else {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pm.onDestroy()
    }

    @Composable
    private fun WeatherApp(
        dialogShown: Boolean,
        weather: List<Weather>,
        currentWeather: Weather,
        onCityChanged: (String) -> Unit,
        onSearchClick: () -> Unit,
        onDialogClosed: () -> Unit
    ) {

        val cityName = remember { mutableStateOf("") }

        Weather_appTheme {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "im1",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Column {
                Row(
                    modifier = Modifier
                        .padding(all = 5.dp)
                        .background(color = Purple200),
                ) {
                    IconButton(
                        onClick = {
                            onSearchClick()
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_search_24),
                            contentDescription = "im3",
                            tint = Color.White
                        )
                    }
                    if (dialogShown) {
                        AlertDialog(
                            onDismissRequest = {
                                onDialogClosed()
                            },
                            title = {
                                Text(text = "Enter city name")
                            },
                            text = {
                                Column {
                                    TextField(
                                        value = cityName.value,
                                        onValueChange = {
                                            cityName.value = it
                                        }
                                    )
                                }
                            },
                            buttons = {
                                Row(
                                    modifier = Modifier.padding(all = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = {
                                            onCityChanged(cityName.value)
                                        }
                                    ) {
                                        Text("Ok")
                                    }
                                }
                            }
                        )
                    }

                }
                WeatherCard(currentWeather)
                WeeklyWeatherList(weather)
            }
        }
    }
}


