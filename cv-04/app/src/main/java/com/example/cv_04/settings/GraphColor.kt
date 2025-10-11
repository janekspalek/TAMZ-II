package com.example.cv_04.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

fun Color.toHex(): String {
    return Integer.toHexString(this.toArgb()).uppercase().takeLast(6)
}
@Composable
fun GraphColor(
    primaryColor: Color,
    onPrimaryColorChange: (Color) -> Unit,
    secondaryColor: Color,
    onSecondaryColorChange: (Color) -> Unit
) {
    var showColorPickerPrimary by remember { mutableStateOf(false) }
    var showColorPickerSecondary by remember { mutableStateOf(false) }

    Text("Barva grafu",
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold
    )

    Row (
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(50.dp)
            .clickable {
                showColorPickerPrimary = !showColorPickerPrimary;
                showColorPickerSecondary = false
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            "Primarni",
            modifier = Modifier.padding(start = 16.dp),
        )
        Row (verticalAlignment = Alignment.CenterVertically, ){
            Text(
                "#${primaryColor.toHex()}"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .background(primaryColor, shape = CircleShape)
                    .height(20.dp)
                    .width(20.dp)
            )
        }
    }

    Row (
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(50.dp)
            .clickable {
                showColorPickerSecondary = !showColorPickerSecondary;
                showColorPickerPrimary = false
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            "Sekundarni",
            modifier = Modifier.padding(start = 16.dp),
        )
        Row (verticalAlignment = Alignment.CenterVertically, ){
            Text(
                "#${secondaryColor.toHex()}"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .background(secondaryColor, shape = CircleShape)
                    .height(20.dp)
                    .width(20.dp)
            )
        }
    }

    if(showColorPickerPrimary) {
        HsvColorPicker(
            initialColor = primaryColor,
            modifier = Modifier.fillMaxWidth()
                .height(450.dp)
                .padding(10.dp),
            controller = rememberColorPickerController(),
            onColorChanged = {envelope ->
                onPrimaryColorChange(envelope.color)
            }
        )
    }

    if(showColorPickerSecondary) {
        HsvColorPicker(
            initialColor = secondaryColor,
            modifier = Modifier.fillMaxWidth()
                .height(450.dp)
                .padding(10.dp),
            controller = rememberColorPickerController(),
            onColorChanged = {envelope ->
                onSecondaryColorChange(envelope.color)
            }

        )
    }
}