package com.example.cv_08.data

import androidx.compose.runtime.mutableStateListOf

object DataHolder {
    var forecastData: WeatherApiResponse? = null
    var selectedCity: String? = null
    var currentCity: String = "ostrava"
    val favoriteCities = mutableStateListOf<String>()
}