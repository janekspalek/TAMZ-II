package com.example.cv_04.home

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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cv_04.ChartType
import com.example.cv_04.MyDropdownMenu
import kotlin.math.pow
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    chartType: ChartType,
    primaryColor: Color,
    secondaryColor: Color
) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            TopAppBar (
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
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
        App(modifier = Modifier.padding(innerPadding), chartType, primaryColor, secondaryColor)
    }
}

@Composable
fun App(
    modifier: Modifier = Modifier,
    chartType: ChartType,
    primaryColor: Color,
    secondaryColor: Color
) {
    var deposit by remember { mutableFloatStateOf(10000f) }
    var interest by remember { mutableFloatStateOf(10f) }
    var years by remember { mutableFloatStateOf(5f) }
    var monthlyDeposit by remember { mutableFloatStateOf(500f) }

    val r = interest / 100f / 12f
    val n = years * 12f

    val futureSingleDeposit = deposit * (1 + r).pow(n)

    val futureMonthlyDeposits = if (r != 0f)
        monthlyDeposit * ((1 + r).pow(n) - 1) / r
    else
        monthlyDeposit * n

    val futureValue = futureSingleDeposit + futureMonthlyDeposits

    val totalDeposited = deposit + monthlyDeposit * n

    val interestOnly = futureValue - totalDeposited

    val scrollState = rememberScrollState()

    Column (modifier = modifier.verticalScroll(scrollState).fillMaxSize().padding(20.dp)) {
        Amounts(
            total = futureValue,
            interestOnly = interestOnly
        )

        Spacer(modifier = Modifier.height(20.dp))

        when(chartType) {
            ChartType.BAR -> BarGraph(deposit, interestOnly, primaryColor, secondaryColor);
            ChartType.PIE -> PieGraph(deposit, interestOnly, primaryColor, secondaryColor);
        }

        Spacer(modifier = Modifier.height(20.dp))

        SliderCard(
            deposit,
            onDepositChange = {deposit = it.roundToInt().toFloat()},
            monthlyDeposit,
            onMonthlyDepositChange = {monthlyDeposit = it.roundToInt().toFloat()},
            interest,
            onInterestChange = {interest = it.roundToInt().toFloat()},
            years,
            onYearsChange = {years = it.roundToInt().toFloat()}
        )
    }
}

