package com.example.cv_04.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SliderCard(
    deposit: Float,
    onDepositChange: (Float) -> Unit,
    interest: Float,
    onInterestChange: (Float) -> Unit,
    years: Float,
    onYearsChange: (Float) -> Unit
) {
    ElevatedCard (modifier = Modifier.fillMaxWidth()){
        Column (modifier = Modifier.padding(20.dp)){
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Vklad", fontWeight = FontWeight.SemiBold)
                Text("${deposit.toInt()} Kč")
            }
            Row(modifier = Modifier.fillMaxWidth()){
                Slider(value = deposit, onValueChange = onDepositChange, valueRange = 10000f..100000f, steps = 8)
            }
        }
        Column (modifier = Modifier.padding(20.dp)){
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Úrok", fontWeight = FontWeight.SemiBold)
                Text("${interest.toInt()} %")
            }
            Row(modifier = Modifier.fillMaxWidth()){
                Slider(value = interest, onValueChange = onInterestChange, valueRange = 1f..20f, steps = 19)
            }
        }
        Column (modifier = Modifier.padding(20.dp)){
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Období", fontWeight = FontWeight.SemiBold)
                Text("${years.toInt()} let")
            }
            Row(modifier = Modifier.fillMaxWidth()){
                Slider(value = years, onValueChange = onYearsChange, valueRange = 1f..30f, steps = 29)
            }
        }
    }
}