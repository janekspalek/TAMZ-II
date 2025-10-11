package com.example.cv_04.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    Scaffold (
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            TopAppBar (
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = { Text("Nastavení") },
                navigationIcon = {
                    IconButton (onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Zpět", tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            val radioOptions = listOf("BarChart", "PieChart")
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

            Text("Typ grafu",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                  Row ( Modifier
                      .fillMaxWidth()
                      .height(50.dp)
                      .selectable(
                          selected = (text == selectedOption),
                          onClick = { onOptionSelected(text) },
                          role = Role.RadioButton
                      )
                      .padding(horizontal = 8.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.SpaceBetween,

                  ) {
                      Text(
                          text = text,
                          style = MaterialTheme.typography.bodyLarge,
                          modifier = Modifier.padding(start = 16.dp)
                      )
                      RadioButton(
                          selected = (text == selectedOption),
                          onClick = null
                      )
                  }
                }
            }

            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            Text("Barva grafu",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )


        }

    }
}