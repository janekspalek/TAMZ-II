package com.example.cv_08

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cv_08.forecast.WeatherForecast
import com.example.cv_08.home.HomeScreen
import com.example.cv_08.ui.theme.Cv08Theme

// API KEY c0bd78361293b062285d5a7ed470690d
// https://api.openweathermap.org/data/2.5/forecast?appid=c0bd78361293b062285d5a7ed470690d&q=ostrava

@Composable
fun getWeatherIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> R.drawable.icon_01d
        "01n" -> R.drawable.icon_01n
        "02d" -> R.drawable.icon_02d
        "02n" -> R.drawable.icon_02n
        "03d" -> R.drawable.icon_03d
        "03n" -> R.drawable.icon_03n
        "04d" -> R.drawable.icon_04d
        "04n" -> R.drawable.icon_04n
        "09d" -> R.drawable.icon_09d
        "09n" -> R.drawable.icon_09n
        "10d" -> R.drawable.icon_10d
        "10n" -> R.drawable.icon_10n
        "11d" -> R.drawable.icon_11d
        "11n" -> R.drawable.icon_11n
        "13d" -> R.drawable.icon_13d
        "13n" -> R.drawable.icon_13n
        "50d" -> R.drawable.icon_50d
        "50n" -> R.drawable.icon_50n
        else -> R.drawable.icon_01d
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cv08Theme {
                val navController = rememberNavController();
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                    ) {
                        composable ("home") {
                            HomeScreen(
                                onWeatherForecastClick = {
                                    navController.navigate("forecast")
                                }
                            )
                        }
                        composable ("forecast") {
                            WeatherForecast(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Cv08Theme {
        Greeting("Android")
    }
}