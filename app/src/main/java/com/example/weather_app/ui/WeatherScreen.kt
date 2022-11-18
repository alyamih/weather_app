package com.example.weather_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather_app.model.Weather
import com.example.weather_app.ui.theme.Purple200
import com.google.accompanist.pager.*

@Composable
fun WeatherCard(weather: Weather) {
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = 0.dp,
            backgroundColor = Purple200,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        text = weather .time,
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White,
                        fontWeight = FontWeight.W600
                    )
                    AsyncImage(
                        model = HTTP_SCHEME + weather .icon,
                        contentDescription = "im2",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(top = 8.dp, start = 8.dp)
                    )
                }
                Text(
                    text = weather.city,
                    style = TextStyle(fontSize = 30.sp),
                    color = Color.White,
                    fontWeight = FontWeight.W600
                )
                Text(
                    text = "${weather.currentTemp}°С",
                    style = TextStyle(fontSize = 50.sp),
                    color = Color.White,
                    fontWeight = FontWeight.W600
                )
                Text(
                    text = weather .condition,
                    style = TextStyle(fontSize = 20.sp),
                    color = Color.White,
                    fontWeight = FontWeight.W600
                )
                Text(
                    text = "${weather .max_temp}°С/${weather .min_temp}°С",
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                }
            }
        }
    }
}



private const val HTTP_SCHEME = "https:"