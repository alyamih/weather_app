package com.example.weather_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weather_app.model.Weather

@Composable
fun WeeklyWeatherList(weatherList: List<Weather>) {
    Column(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                weatherList
            ) { _, item ->
                ListItem(item)
            }
        }
    }
}