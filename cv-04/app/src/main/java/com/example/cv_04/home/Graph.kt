package com.example.cv_04.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters

@Composable
fun Graph(deposit: Float, interestOnly: Float) {
    val data: List<BarParameters> = listOf(
        BarParameters(
            dataName = "Vklad",
            data = listOf(deposit.toDouble()),
            barColor = MaterialTheme.colorScheme.primary
        ),
        BarParameters(
            dataName = "Úroky",
            data = listOf(interestOnly.toDouble()),
            barColor = MaterialTheme.colorScheme.secondary
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