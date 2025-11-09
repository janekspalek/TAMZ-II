package com.example.cv_08.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cv_08.data.CityData
import com.example.cv_08.data.ForecastListItem
import com.example.cv_08.getWeatherIcon
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrentWeather (
    city: CityData,
    weather: ForecastListItem
) {
    val dateTime = LocalDateTime.parse(
        weather.dt_txt,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )

    val dateString = dateTime.format(DateTimeFormatter.ofPattern("EEE, d. MMMM", Locale.getDefault()))

    val iconCode = weather.weather.firstOrNull()?.icon ?: "01d"
    val iconResId = getWeatherIcon(iconCode)

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = city.name + ", " + city.country,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = dateString,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card (
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column (
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = weather.weather.firstOrNull()?.main ?: "Ikona počasí",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text (
                    text = weather.weather[0].main,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text (
                    text = weather.main.temp.roundToInt().toString() + "°C",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}