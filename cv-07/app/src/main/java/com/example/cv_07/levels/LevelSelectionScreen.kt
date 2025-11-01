package com.example.cv_07.levels

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cv_07.R
import com.example.cv_07.data.Level

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectionScreen(
    levels: List<Level>
) {
    val context = LocalContext.current
    val bitmaps = remember(context) {
        arrayOf(
            BitmapFactory.decodeResource(context.resources, R.drawable.empty).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.wall).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.box).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.goal).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.hero).asImageBitmap(),
            BitmapFactory.decodeResource(context.resources, R.drawable.boxok).asImageBitmap()
        )
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Levels")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                windowInsets = WindowInsets(0.dp)
            )
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        LazyVerticalGrid (
            modifier = Modifier.padding(innerPadding),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(levels) { level ->
                LevelItem(
                    level,
                    bitmaps = bitmaps,
                    onLevelClick = {
                        println("Kliknuto na: ${level.id}")
                    }
                )
            }
        }
    }
}