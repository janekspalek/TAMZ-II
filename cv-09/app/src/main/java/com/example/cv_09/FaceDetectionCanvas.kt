package com.example.cv_09

import android.R.attr.textSize
import android.R.attr.typeface
import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.face.Face
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun FaceDetectionCanvas(
    bitmap: Bitmap,
    faces: List<Face>,
    isAnonymized: Boolean,
    modifier: Modifier = Modifier
) {
    val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
    ) {
        Canvas (modifier = Modifier.fillMaxSize()) {
            drawImage(
                image = bitmap.asImageBitmap(),
                dstSize = IntSize(size.width.toInt(), size.height.toInt())
            )

            val scaleX = size.width / bitmap.width
            val scaleY = size.height / bitmap.height

            val textPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.YELLOW
                textSize = 40f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
                setShadowLayer(10f, 0f, 0f, android.graphics.Color.BLACK)
            }

            faces.forEachIndexed { index, face ->
                val bounds = face.boundingBox

                val left = bounds.left * scaleX
                val top = bounds.top * scaleY
                val width = bounds.width() * scaleX
                val height = bounds.height() * scaleY

                if (isAnonymized) {
                    drawRect(
                        color = Color.Black,
                        style = Fill,
                        topLeft = Offset(left, top),
                        size = Size(width, height)
                    )
                } else {
                    drawRect(
                        color = Color.Red,
                        style = Stroke(width = 3.dp.toPx()),
                        topLeft = Offset(left, top),
                        size = Size(width, height)
                    )

                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawText(
                            "Face ${index + 1}",
                            left - 20,
                            top - 10,
                            textPaint
                        )
                    }
                }
            }
        }
    }
}