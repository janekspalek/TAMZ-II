package com.example.cv_06

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cv_06.ui.theme.Cv06Theme
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cv06Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


private const val LEVEL_WIDTH = 10
private const val LEVEL_HEIGHT = 10

// Data levelu
private val levelData = intArrayOf(
    1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
    1, 0, 0, 0, 0, 0, 0, 0, 1, 0,
    1, 0, 2, 3, 3, 2, 1, 0, 1, 0,
    1, 0, 1, 3, 2, 3, 2, 0, 1, 0,
    1, 0, 2, 3, 3, 2, 4, 0, 1, 0,
    1, 0, 1, 3, 2, 3, 2, 0, 1, 0,
    1, 0, 2, 3, 3, 2, 1, 0, 1, 0,
    1, 0, 0, 0, 0, 0, 0, 0, 1, 0,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0
)

enum class DIRECTION {
    UP, DOWN, RIGHT, LEFT
}

private fun findPlayer(currentLevel: List<Int>) : Pair<Int, Int> {
    val playerIndex = currentLevel.indexOf(4)
    if(playerIndex == -1) return Pair(0, 0)

    val x = playerIndex % LEVEL_WIDTH
    val y = playerIndex / LEVEL_WIDTH
    return Pair(x, y)
}

private fun movePlayer(
    direction: DIRECTION,
    currentLevel: List<Int>,
    floorData: List<Int>
): List<Int>? {
    val (currentX, currentY) = findPlayer(currentLevel)
    val oldIndex = currentY * LEVEL_WIDTH + currentX

    val (targetX, targetY) = when(direction) {
        DIRECTION.RIGHT -> Pair(currentX + 1, currentY)
        DIRECTION.LEFT -> Pair(currentX - 1, currentY)
        DIRECTION.UP -> Pair(currentX, currentY - 1)
        DIRECTION.DOWN -> Pair(currentX, currentY + 1)
    }

    if(targetX < 0 || targetX >= LEVEL_WIDTH || targetY < 0 || targetY >= LEVEL_HEIGHT) {
        return null
    }

    val targetIndex = targetY * LEVEL_WIDTH + targetX
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

            if(nextToBoxX < 0 || nextToBoxX >= LEVEL_WIDTH || nextToBoxY < 0 || nextToBoxY >= LEVEL_HEIGHT) {
                return null
            }

            val nextToBoxIndex = nextToBoxY * LEVEL_WIDTH + nextToBoxX
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

@Composable
fun GameScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        GameLevelCanvas(levelData = levelData.toList())
    }
}

@Composable
fun GameLevelCanvas(levelData: List<Int>, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var level by remember { mutableStateOf(levelData) }
    val floorData = levelData.map {
        when (it) {
            2, 4 -> 0
            5 -> 3
            else -> it
        }
    }

    val isGameWon = !level.contains(3)

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
                    val newLevel = movePlayer(direction, level, floorData)
                    if(newLevel != null) {
                        level = newLevel
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
            val tileWidthPx = size.width / LEVEL_WIDTH
            val tileHeightPx = size.height / LEVEL_HEIGHT
            val tileSize = minOf(tileWidthPx, tileHeightPx)

            for (y in 0 until LEVEL_HEIGHT) {
                for (x in 0 until LEVEL_WIDTH) {
                    val tileIndex = level[y * LEVEL_WIDTH + x]
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
                    Button( onClick = {
                        level = levelData
                    }) {
                        Text("Play again \uD83C\uDFB0", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameLevelCanvasPreview() {
    Cv06Theme {
        GameScreen()
    }
}
