package com.example.cv_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.example.cv_03.ui.theme.Cv03Theme
import kotlin.math.pow
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cv03Theme {
                Scaffold(modifier = Modifier.fillMaxSize().safeDrawingPadding()) { innerPadding ->
                    App(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    var deposit by remember { mutableFloatStateOf(1000f) }
    var interest by remember { mutableFloatStateOf(10f) }
    var years by remember { mutableFloatStateOf(5f) }

    val futureValue = deposit * (1 + interest / 100).pow(years)
    val interestOnly = futureValue - deposit

    Column (Modifier.fillMaxSize().padding(20.dp)) {
        Amounts(
            total = futureValue,
            interestOnly = interestOnly
        )

        Spacer(modifier = Modifier.height(20.dp))
        SliderCard(
            deposit,
            onDepositChange = {deposit = it.roundToInt().toFloat()},
            interest,
            onInterestChange = {interest = it.roundToInt().toFloat()},
            years,
            onYearsChange = {years = it.roundToInt().toFloat()}
        )
    }
}

@Composable
fun Amounts(total: Float, interestOnly: Float) {
    ElevatedCard (modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
            Column (
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Naspořená suma",
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${"%.0f".format(total)} Kč",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Z toho úroky",
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${"%.0f".format(interestOnly)} Kč",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun Graph() {
    val data: List<BarParameters> = listOf(
        BarParameters(
            dataName = "sdfsdf",
            data = listOf(70.0, 00.0, 50.33, 40.0, 100.500, 50.0),
            barColor = Color.Red
        )
    )

    BarChart(chartParameters = data)
}

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