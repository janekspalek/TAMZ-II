package com.example.cv_07.game

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cv_07.R
import com.example.cv_07.data.Level
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun GameLevelCanvas(
    level: Level,
    onNextLevel: () -> Unit,
    onPrevLevel: () -> Unit,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var currentTiles by remember(level.id) { mutableStateOf(level.tiles) }
    val floorData = level.floor

    val isGameWon = !currentTiles.contains(3)

    var dragStartOffset by remember { mutableStateOf(Offset.Zero) }
    var currentDragOffset by remember { mutableStateOf(Offset.Zero) }
    val minSwipeDistance = 100

    val tiles = remember {
        arrayOf(
            BitmapFactory.decodeResource(context.resources, R.drawable.empty).asImageBitmap(), // 0
            BitmapFactory.decodeResource(context.resources, R.drawable.wall).asImageBitmap(),  // 1
            BitmapFactory.decodeResource(context.resources, R.drawable.box).asImageBitmap(),   // 2
            BitmapFactory.decodeResource(context.resources, R.drawable.goal).asImageBitmap(),  // 3
            BitmapFactory.decodeResource(context.resources, R.drawable.hero).asImageBitmap(),  // 4
            BitmapFactory.decodeResource(context.resources, R.drawable.boxok).asImageBitmap()  // 5
        )
    }

    Box(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
        detectDragGestures (
            onDragStart = {dragStartOffset = it},
            onDrag = {change, offset ->
                currentDragOffset = change.position
            },
            onDragEnd = {
                val dragVector = currentDragOffset - dragStartOffset
                val (dx, dy) = dragVector
                var direction: DIRECTION? = null
                if(abs(dx) > abs(dy)) {
                    if(dx > minSwipeDistance) direction = DIRECTION.RIGHT
                    else if(dx < -minSwipeDistance) direction = DIRECTION.LEFT
                }
                else {
                    if(dy > minSwipeDistance) direction = DIRECTION.DOWN
                    else if(dy < -minSwipeDistance) direction = DIRECTION.UP
                }
                if(direction != null) {
                    val newLevel = movePlayer(
                        direction,
                        currentTiles,
                        floorData,
                        level.width,
                        level.height)
                    if(newLevel != null) {
                        currentTiles = newLevel
                    }
                }
                dragStartOffset = Offset.Zero
                currentDragOffset = Offset.Zero
            }
        )
    }) {
        Canvas(modifier = modifier
            .fillMaxSize()
        ) {
            val tileWidthPx = size.width / level.width
            val tileHeightPx = size.height / level.height
            val tileSize = minOf(tileWidthPx, tileHeightPx)

            for (y in 0 until level.height) {
                for (x in 0 until level.width) {
                    val tileIndex = currentTiles[y * level.width + x]
                    val img: ImageBitmap = tiles[tileIndex.coerceIn(0, tiles.lastIndex)]

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

        if(!isGameWon) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { currentTiles = level.tiles},
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Restart"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Restart")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onPrevLevel,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Previous level"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Previous level")
                    }
                    Button(onClick = onNextLevel,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Next level")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                            contentDescription = "Next level"
                        )
                    }
                }

            }
        }

        if(isGameWon) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Text("\uD83E\uDD13", fontSize = 100.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("LEVEL COMPLETED", fontSize = 30.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button( onClick = onNextLevel) {
                        Text("Next level \uD83C\uDFB0", fontSize = 20.sp)
                    }
                    Button( onClick = {
                        currentTiles = level.tiles
                    }) {
                        Text("Play again \uD83C\uDFB0", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

private fun movePlayer(
    direction: DIRECTION,
    currentLevel: List<Int>,
    floorData: List<Int>,
    levelWidth: Int,
    levelHeight: Int
): List<Int>? {
    val (currentX, currentY) = findPlayer(currentLevel, levelWidth)
    val oldIndex = currentY * levelWidth + currentX

    val (targetX, targetY) = when(direction) {
        DIRECTION.RIGHT -> Pair(currentX + 1, currentY)
        DIRECTION.LEFT -> Pair(currentX - 1, currentY)
        DIRECTION.UP -> Pair(currentX, currentY - 1)
        DIRECTION.DOWN -> Pair(currentX, currentY + 1)
    }

    if(targetX < 0 || targetX >= levelWidth || targetY < 0 || targetY >= levelHeight) {
        return null
    }

    val targetIndex = targetY * levelWidth + targetX
    val tileOnTarget = currentLevel[targetIndex]

    val newList = currentLevel.toMutableList()

    when (tileOnTarget) {
        // wall
        1 -> return null

        // empty space or x
        0, 3 -> {
            newList[targetIndex] = 4
            newList[oldIndex] = floorData[oldIndex]
            return newList
        }

        // box or box on x
        2, 5 -> {
            val (nextToBoxX, nextToBoxY) = when(direction) {
                DIRECTION.RIGHT -> Pair(targetX + 1, targetY)
                DIRECTION.LEFT -> Pair(targetX - 1, targetY)
                DIRECTION.UP -> Pair(targetX, targetY - 1)
                DIRECTION.DOWN -> Pair(targetX, targetY + 1)
            }

            if(nextToBoxX < 0 || nextToBoxX >= levelWidth || nextToBoxY < 0 || nextToBoxY >= levelHeight) {
                return null
            }

            val nextToBoxIndex = nextToBoxY * levelWidth + nextToBoxX
            val tileNextToBox = currentLevel[nextToBoxIndex]

            if(tileNextToBox == 0 || tileNextToBox == 3) {
                newList[nextToBoxIndex] = if (tileNextToBox == 3) 5 else 2
                newList[targetIndex] = 4
                newList[oldIndex] = floorData[oldIndex]
                return newList
            }
            else {
                return null
            }
        }

        else -> return null
    }
}

enum class DIRECTION {
    UP, DOWN, RIGHT, LEFT
}

private fun findPlayer(currentLevel: List<Int>, levelWidth: Int) : Pair<Int, Int> {
    val playerIndex = currentLevel.indexOf(4)
    if(playerIndex == -1) return Pair(0, 0)

    val x = playerIndex % levelWidth
    val y = playerIndex / levelWidth
    return Pair(x, y)
}
