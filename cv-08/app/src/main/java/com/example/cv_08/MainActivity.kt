package com.example.cv_08

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cv_08.data.DataHolder
import com.example.cv_08.favourites.FavouritesScreen
import com.example.cv_08.forecast.WeatherForecast
import com.example.cv_08.home.HomeScreen
import com.example.cv_08.ui.theme.Cv08Theme

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

enum class Destination (
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    HOME("home", "Home", Icons.Default.Home),
    FORECAST("forecast", "Forecast", Icons.AutoMirrored.Filled.List),
    FAVOURITES("favourites", "My Favourites", Icons.Outlined.Favorite)
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("weather_app", MODE_PRIVATE)
        val favouriteCities = prefs.getStringSet("favourite_cities", emptySet())
        if (favouriteCities != null) {
            DataHolder.favoriteCities.addAll(favouriteCities)
        }

        // enableEdgeToEdge()
        setContent {
            Cv08Theme {
                val navController = rememberNavController();
                val startDestination = Destination.HOME
                var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar (windowInsets = NavigationBarDefaults.windowInsets) {
                           Destination.entries.forEachIndexed { index, destination ->
                               NavigationBarItem(
                                   selected = selectedDestination == index,
                                   onClick = {
                                       navController.navigate(route = destination.route)
                                       selectedDestination = index
                                   },
                                   icon = {
                                       Icon(
                                           destination.icon,
                                           contentDescription = destination.name
                                       )
                                   },
                                   label = {Text(destination.label)}
                               )
                           }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination.route,
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
                            WeatherForecast()
                        }
                        composable ("favourites") {
                            FavouritesScreen(
                                onCitySelected = { cityName ->
                                    DataHolder.selectedCity = cityName
                                    navController.navigate(Destination.HOME.route)
                                    selectedDestination = 0
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