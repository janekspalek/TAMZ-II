package com.example.cv_04.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cv_04.ChartType

@Composable
fun GraphType( onChartSelected: (ChartType) -> Unit,
               currentChart: ChartType) {
    val radioOptions = listOf(ChartType.BAR, ChartType.PIE)

    ElevatedCard (modifier = Modifier.fillMaxWidth()) {
        Column (modifier = Modifier.padding(20.dp)) {
            Text("Typ grafu",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { option ->
                    Row ( Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .selectable(
                            selected = (option == currentChart),
                            onClick = { onChartSelected(option) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = option.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        RadioButton(
                            selected = (option == currentChart),
                            onClick = null
                        )
                    }
                }
            }
        }
    }
}