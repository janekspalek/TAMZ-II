package com.example.cv_08.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cv_08.data.WeatherApiResponse
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.Line
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastChart(forecast: WeatherApiResponse) {
    val tempValues = remember(forecast) {
        forecast.list.map { it.main.temp }
    }

    val timeLabels = remember(forecast) {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

        forecast.list.map {
            val date = LocalDateTime.parse(it.dt_txt, inputFormatter)
            "${date.format(dayFormatter)}\n${date.format(timeFormatter)}"
        }
    }

    val minTemp = (tempValues.minOrNull() ?: 0.0) - 5.0
    val maxTemp = (tempValues.maxOrNull() ?: 30.0) + 5.0

    val chartColor = MaterialTheme.colorScheme.primary

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),

        data = remember {
            listOf(
                Line(
                    label = "Temperature (°C)",
                    values = tempValues,
                    color = SolidColor(chartColor),
                    firstGradientFillColor = chartColor.copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                )
            )
        },

        minValue = minTemp,
        maxValue = maxTemp
    )

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 56.dp)
    ) {
        timeLabels.forEachIndexed { index, label ->
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (index % 6 == 0) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp,
                        maxLines = 2,
                        lineHeight = 12.sp,
                        modifier = Modifier.requiredWidth(50.dp)
                    )
                }
            }
        }
    }
}