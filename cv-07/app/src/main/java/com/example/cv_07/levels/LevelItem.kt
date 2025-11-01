package com.example.cv_07.levels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.example.cv_07.data.Level

@Composable
fun LevelItem(
    level: Level,
    bitmaps: Array<ImageBitmap>,
    onLevelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier
            .fillMaxWidth()
            .clickable { onLevelClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LevelCanvas(
                level.tiles,
                level.width,
                level.height,
                bitmaps,
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(8.dp).background(MaterialTheme.colorScheme.background)
            )

            Text(
                text = "Level " + level.id,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}