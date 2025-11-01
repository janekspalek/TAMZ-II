package com.example.cv_07.levels

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

@Composable
fun LevelCanvas(
    tiles: List<Int>,
    levelWidth: Int,
    levelHeight: Int,
    bitmaps: Array<ImageBitmap>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier
    ) {
        val tileWidthPx = size.width / levelWidth
        val tileHeightPx = size.height / levelHeight
        val tileSize = minOf(tileWidthPx, tileHeightPx)

        for (y in 0 until levelHeight) {
            for (x in 0 until levelWidth) {
                val index = y * levelWidth + x
                val tileIndex = tiles[index]
                val img: ImageBitmap = bitmaps[tileIndex.coerceIn(0, tiles.lastIndex)]

                drawImage(
                    image = img,
                    dstOffset = IntOffset(
                        x = (x * tileSize).roundToInt(),
                        y = (y * tileSize).roundToInt()
                    ),
                    dstSize = IntSize(
                        width = tileSize.roundToInt(),
                        height = tileSize.roundToInt()
                    )
                )
            }
        }
    }
}