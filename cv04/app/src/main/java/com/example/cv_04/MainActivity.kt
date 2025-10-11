package com.example.cv_04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cv_04.ui.theme.Cv04Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cv04Theme {
                val navController = rememberNavController();

                NavHost(navController = navController, startDestination = "home") {
                    composable ("home") {
                        HomeScreen (onNavigateToSettings = {
                            navController.navigate("settings")
                        })
                    }
                    composable ("settings") {
                        SettingsScreen(onNavigateBack = {
                            navController.popBackStack()
                        })
                    }
                }
            }
        }
    }
}