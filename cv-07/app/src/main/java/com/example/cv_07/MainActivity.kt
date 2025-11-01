package com.example.cv_07

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cv_07.data.Level
import com.example.cv_07.data.LevelRepository
import com.example.cv_07.game.GameLevelCanvas
import com.example.cv_07.game.GameScreen
import com.example.cv_07.levels.LevelSelectionScreen
import com.example.cv_07.ui.theme.Cv07Theme
import kotlin.math.abs
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cv07Theme {
                val navController = rememberNavController();

                val context = LocalContext.current
                val levels = remember { LevelRepository.loadLevels(context) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "levels",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable ("levels") {
                            LevelSelectionScreen(
                                levels,
                                onLevelClick = {levelId ->
                                    navController.navigate("game/$levelId")
                                }
                            )
                        }
                        composable(route = "game/{levelId}",
                            arguments = listOf(navArgument("levelId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val levelId = backStackEntry.arguments?.getInt("levelId") ?: 0
                                val levelToPlay = levels.getOrNull(levelId)

                                if(levelToPlay != null) {
                                    GameScreen(
                                        level = levelToPlay,
                                        onNextLevel = {
                                            val nextLevelId = levelId + 1
                                            if(nextLevelId < levels.size) {
                                                navController.navigate("game/$nextLevelId") {
                                                    popUpTo("game/$levelId") { inclusive = true }
                                                }
                                            }
                                            else {
                                                navController.popBackStack("levels", false)
                                            }
                                        },
                                        onPrevLevel = {
                                            val prevLevelId = levelId - 1
                                            if(prevLevelId < levels.size) {
                                                navController.navigate("game/$prevLevelId") {
                                                    popUpTo("game/$levelId") { inclusive = true }
                                                }
                                            }
                                            else {
                                                navController.popBackStack("levels", false)
                                            }
                                        },
                                        onBackToMenu = {
                                            navController.popBackStack()
                                        }
                                    )
                                }
                            else {
                               Column (
                                   modifier = Modifier.padding(16.dp)
                               ){
                                   Text("Level was not found")
                                   Button(
                                       onClick = {navController.popBackStack()}
                                   ) {
                                       Text("Go back to menu")
                                   }
                               }
                            }
                        }
                    }
                }
            }
        }
    }
}






