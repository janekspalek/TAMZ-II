package com.example.cv_05

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// UPC-A L a R patterny
private val Lp = intArrayOf(
    0x0D, // 0001101
    0x19, // 0011001
    0x13, // 0010011
    0x3D, // 0111101
    0x23, // 0100011
    0x31, // 0110001
    0x2F, // 0101111
    0x3B, // 0111011
    0x37, // 0110111
    0x0B  // 0001011
)

private val Rp = intArrayOf(
    0x72, // 1110010
    0x66, // 1100110
    0x6C, // 1101100
    0x42, // 1000010
    0x5C, // 1011100
    0x4E, // 1001110
    0x50, // 1010000
    0x44, // 1000100
    0x48, // 1001000
    0x74  // 1110100
)

@Composable
fun BarCode (
    modifier: Modifier = Modifier,
    code: String
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas (
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val widthPerBar = size.width / 95f
        val height = size.height

        val fullPattern = mutableListOf<Int>()
        fullPattern.addAll(listOf(1,0,1)) // left guard

        // 6 left digits
        for(i in 0 until 6) {
            val digit = code[i].digitToInt()
            val pattern = Lp[digit]
            for (bit in 6 downTo 0) { // 7 bitů
                fullPattern.add((pattern shr bit) and 1)
            }
        }

        // middle guard (01010)
        fullPattern.addAll(listOf(0,1,0,1,0))

        // 6 right digits
        for (i in 6 until 12) {
            val digit = code[i].digitToInt()
            val pattern = Rp[digit]
            for (bit in 6 downTo 0) {
                fullPattern.add((pattern shr bit) and 1)
            }
        }

        // end guard (101)
        fullPattern.addAll(listOf(1,0,1))

        fullPattern.forEachIndexed { index, bit ->
            if (bit == 1) {
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(x = index * widthPerBar, y = 0f),
                    size = Size(widthPerBar, height)
                )
            }
        }
        val textHeight = 25.dp.toPx()
        val textY = size.height - textHeight

        val totalWidth = size.width
        val spacing = totalWidth / 12f

        drawRect(
            color = Color.White,
            topLeft = Offset(x = 0f, y = size.height - 75f),
            size = Size(size.width, 75f)
        )

        for (i in 0 until 12) {
            val x = spacing * i + spacing / 2   // střed každé "buňky"
            drawText(
                textMeasurer,
                text = code[i].toString(),
                style = TextStyle(
                    fontSize = 25.sp,
                    color = Color.Black,
                ),
                topLeft = Offset(x = x - 12f, y = textY)
            )
        }
    }
}