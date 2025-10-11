package com.example.cv_04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cv_04.home.HomeScreen
import com.example.cv_04.settings.SettingsScreen
import com.example.cv_04.ui.theme.Cv04Theme

enum class ChartType {
    BAR, PIE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cv04Theme {
                val navController = rememberNavController();
                var chartType by remember { mutableStateOf(ChartType.BAR) }
                var primaryColor by remember { mutableStateOf(Color.Black) }
                var secondaryColor by remember { mutableStateOf(Color.Black) }

                NavHost(navController = navController, startDestination = "home") {
                    composable ("home") {
                        HomeScreen(onNavigateToSettings = {
                            navController.navigate("settings")
                        }, chartType, primaryColor, secondaryColor)
                    }
                    composable ("settings") {
                        SettingsScreen(
                            onNavigateBack = { navController.popBackStack() },
                            currentChart = chartType,
                            onChartSelected = { newChartType -> chartType = newChartType },
                            primaryColor,
                            onPrimaryColorChange = { newColor -> primaryColor = newColor },
                            secondaryColor,
                            onSecondaryColorChange = { newColor -> secondaryColor = newColor }
                        )
                    }
                }
            }
        }
    }
}