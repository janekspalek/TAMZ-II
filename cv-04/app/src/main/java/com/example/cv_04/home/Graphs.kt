package com.example.cv_04.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData

@Composable
fun BarGraph(deposit: Float, interestOnly: Float, primaryColor: Color, secondaryColor: Color) {
    val data: List<BarParameters> = listOf(
        BarParameters(
            dataName = "Vklad",
            data = listOf(deposit.toDouble()),
            barColor = primaryColor
        ),
        BarParameters(
            dataName = "Úroky",
            data = listOf(interestOnly.toDouble()),
            barColor = secondaryColor
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
fun PieGraph(
    deposit: Float,
    interestOnly: Float,
    primaryColor: Color,
    secondaryColor: Color) {
    val data: List<PieChartData> = listOf(
        PieChartData(
            partName = "Vklad",
            data = deposit.toDouble(),
            color = primaryColor
        ),
        PieChartData(
            partName = "Úroky",
            data = interestOnly.toDouble(),
            color = secondaryColor
        )
    )

    ElevatedCard (modifier = Modifier.fillMaxWidth().height(400.dp)) {
        Column (modifier = Modifier.padding(20.dp)) {
            PieChart(
                pieChartData = data
            )
        }
    }
}