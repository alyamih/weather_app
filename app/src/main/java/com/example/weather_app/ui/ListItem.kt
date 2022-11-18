package com.example.weather_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

@Composable
fun ListItem(item: Weather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        backgroundColor = Purple200,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.padding(start = 8.dp, top = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    text = item.time,
                    color = Color.White,
                    style = TextStyle(fontSize = 12.sp),
                    fontWeight = FontWeight.W600
                )
                Text(
                    text = item.condition,
                    color = Color.White,
                    style = TextStyle(fontSize = 15.sp),
                    fontWeight = FontWeight.W600
                )
            }
            Text(
                text = item.currentTemp.ifEmpty {
                    "${item.max_temp}°С/${item.min_temp}°С"
                },
                color = Color.White,
                style = TextStyle(fontSize = 18.sp),
                fontWeight = FontWeight.W600
            )
            AsyncImage(
                model = "https:${item.icon}",
                contentDescription = "im2",
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 8.dp)
            )
        }
    }
}