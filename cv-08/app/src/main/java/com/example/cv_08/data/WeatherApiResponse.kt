package com.example.cv_08.data

import kotlinx.serialization.Serializable

@Serializable
data class WeatherApiResponse(
    val list: List<ForecastListItem>,
    val city: CityData,
    val cod: String
)
@Serializable
data class ForecastListItem(
    val main: MainData,
    val weather: List<WeatherData>,
    val dt: Long,
    val dt_txt: String
)
@Serializable
data class MainData(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int
)

@Serializable
data class WeatherData(
    val main: String,
    val description: String,
    val icon: String
)
@Serializable
data class CityData(
    val name: String,
    val country: String
)

/*
{
    "cod": "200",
    "message": 0,
    "cnt": 40,
    "list": [
    {
        "dt": 1762700400,
        "main": {
        "temp": 280.41,
        "feels_like": 277.93,
        "temp_min": 280.26,
        "temp_max": 280.41,
        "pressure": 1016,
        "sea_level": 1016,
        "grnd_level": 989,
        "humidity": 78,
        "temp_kf": 0.15
    },
        "weather": [
        {
            "id": 804,
            "main": "Clouds",
            "description": "overcast clouds",
            "icon": "04d"
        }
        ],
        "clouds": {
        "all": 87
    },
        "wind": {
        "speed": 3.73,
        "deg": 212,
        "gust": 6.92
    },
        "visibility": 10000,
        "pop": 0,
        "sys": {
        "pod": "d"
    },
        "dt_txt": "2025-11-09 15:00:00"
    },*/