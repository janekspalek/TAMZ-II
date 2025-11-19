package com.example.cv_08.home

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cv_08.data.CityData
import com.example.cv_08.data.DataHolder
import com.example.cv_08.data.ForecastListItem
import com.example.cv_08.getWeatherIcon
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt
import androidx.core.content.edit

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

    val isFavorite = DataHolder.favoriteCities.contains(city.name)

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = city.name + ", " + city.country,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )
            val context = LocalContext.current

            IconButton(
                onClick = {
                    if (isFavorite) {
                        DataHolder.favoriteCities.remove(city.name)
                    } else {
                        DataHolder.favoriteCities.add(city.name)
                    }

                    val prefs = context.getSharedPreferences("weather_app", Context.MODE_PRIVATE)
                    prefs.edit { putStringSet("favourite_cities", DataHolder.favoriteCities.toSet()) }
                }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Save to favourites"
                )
            }
        }

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