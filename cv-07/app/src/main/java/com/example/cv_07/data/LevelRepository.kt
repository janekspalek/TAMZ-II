package com.example.cv_07.data

import android.content.Context

object LevelRepository {
    private var loadedLevels: List<Level> = emptyList()

    fun loadLevels(context: Context, levelsFilename: String = "levels.txt"): List<Level> {
        if(loadedLevels.isNotEmpty()) {
            return loadedLevels
        }

        val levels = mutableListOf<Level>()
        val levelLines = mutableListOf<String>()
        var levelCounter = 0

        try {
            context.assets.open(levelsFilename).bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    if(line.startsWith("Level")) { // new level
                        if(levelLines.isNotEmpty()) {
                            levels.add(parseLevel(levelCounter ++, levelLines))
                            levelLines.clear()
                            println("Adding level $levelCounter")
                        }
                    } else if(line.isBlank()) { // end of level
                        if(levelLines.isNotEmpty()) {
                            levels.add(parseLevel(levelCounter ++, levelLines))
                            levelLines.clear()
                            println("Adding level $levelCounter")
                        }
                    } else if(line.any { it in "#$@.* " }) { // row with level data
                        levelLines.add(line)
                    }
                }

                if (levelLines.isNotEmpty()) { // load last level
                    levels.add(parseLevel(levelCounter++, levelLines))
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        loadedLevels = levels
        return loadedLevels
    }

    private fun parseLevel(id: Int, lines: List<String>): Level {
        val height = lines.size
        val width = lines.maxOf { it.length }

        val tiles = mutableListOf<Int>()
        val floor = mutableListOf<Int>()

        for(y in 0 until height) {
            val line = lines[y]

            for(x in 0 until width) {
                val char = line.getOrNull(x) ?: ' ' // if the line is shorter, add blank space

                // Symbols used in the screen files:
                // " " - An empty space (floor)
                // "#" - Walls (the walls will be curved and shadowed automatically)
                // "." - Target or goal square
                // "$" - Treasure
                // "*" - Treasure on a goal area
                // "@" - You (the player)
                // "+" - You standing on a goal square.
                val(tile, floorTile) = when(char) {
                    ' ' -> Pair(0, 0) // empty space
                    '#' -> Pair(1, 1) // wall
                    '$' -> Pair(2, 0) // treasure
                    '.' -> Pair(3, 3) // goal
                    '*' -> Pair(5, 3) // treasure on goal
                    '@' -> Pair(4, 0) // player
                    '+' -> Pair(4, 3) // player on goal
                    else -> Pair(0, 0)
                }
                tiles.add(tile)
                floor.add(floorTile)
            }
        }

        return Level(id, width, height, floor, tiles)
    }
}