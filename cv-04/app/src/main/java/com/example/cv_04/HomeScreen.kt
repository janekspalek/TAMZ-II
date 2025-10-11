package com.example.cv_04

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import kotlin.math.pow
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigateToSettings: () -> Unit) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            TopAppBar (
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =  Color(0xFF1565C0),
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Domů")
                },
                actions = {
                    MyDropdownMenu(onNavigateToSettings)
                },
            )
        },
    ) { innerPadding ->
        App(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    var deposit by remember { mutableFloatStateOf(1000f) }
    var interest by remember { mutableFloatStateOf(10f) }
    var years by remember { mutableFloatStateOf(5f) }

    val futureValue = deposit * (1 + interest / 100).pow(years)
    val interestOnly = futureValue - deposit

    val scrollState = rememberScrollState()

    Column (modifier = modifier.verticalScroll(scrollState).fillMaxSize().padding(20.dp)) {
        Amounts(
            total = futureValue,
            interestOnly = interestOnly
        )

        Spacer(modifier = Modifier.height(20.dp))

        Graph(deposit, interestOnly)

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
fun Graph(deposit: Float, interestOnly: Float) {
    val data: List<BarParameters> = listOf(
        BarParameters(
            dataName = "Vklad",
            data = listOf(deposit.toDouble()),
            barColor = Color(0xFF1565C0)
        ),
        BarParameters(
            dataName = "Úroky",
            data = listOf(interestOnly.toDouble()),
            barColor = Color(0xFFFFA726)
        )
    )

    ElevatedCard (modifier = Modifier.fillMaxWidth().height(400.dp)) {
        Column (modifier = Modifier.padding(20.dp)) {
            BarChart(
                chartParameters = data,
                xAxisData = listOf("Vklad/Uroky",),
                animateChart = true,
                barWidth = 130.dp,
                barCornerRadius = 15.dp
            )
        }
    }
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

