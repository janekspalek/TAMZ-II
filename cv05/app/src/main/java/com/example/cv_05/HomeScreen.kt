package com.example.cv_05

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.cv_05.ui.theme.BarCode

fun isValidUPC(code: String): Boolean {
    if(code.length != 12) return false
    val digits = IntArray(12)

    for (i in 0 until 12) { // convert upc code to int array
        val number = code[i]
        if(!number.isDigit()) return false
        digits[i] = number.digitToInt()
    }

    var oddSum = 0;
    for(i in 0 until 11 step 2) {
        oddSum += digits[i]
    }
    oddSum *= 3

    var evenSum = 0
    for(i in 1 until 11 step 2) {
        evenSum += digits[i]
    }

    val totalSum = oddSum + evenSum

    var remainder = totalSum % 10
    if(remainder != 0) remainder = 10 - remainder

    return remainder == digits[11]
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen( modifier: Modifier = Modifier) {
    var barCodeText by remember { mutableStateOf("042100005264") }
    var barCodeTextConfirmed by remember { mutableStateOf("042100005264") }
    val openAlertDialog = remember { mutableStateOf(false) }

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xF4F3FAFF)),
    ){
        ElevatedCard (
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color.White
            )
        ){
           Column(
               modifier = Modifier.padding(32.dp),
               verticalArrangement = Arrangement.Center,
           ) {
               BarCode(code = barCodeTextConfirmed)
           }
        }

        Column (modifier = Modifier.padding(16.dp)) {
            TextField(
                value = barCodeText,
                onValueChange = {barCodeText = it},
                modifier = Modifier.fillMaxWidth(),
                placeholder = {Text("Enter 12-digit UPC code")}
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if(isValidUPC(barCodeText)) {
                        barCodeTextConfirmed = barCodeText
                    }
                    else {
                        openAlertDialog.value = true
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Generate UPC code")
            }
        }

        if (openAlertDialog.value) {
            AlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                confirmButton = {
                    Button(
                        onClick = { openAlertDialog.value = false },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("OK")
                    }
                },
                title = {
                    Text("Invalid UPC Code",
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        },
                text = { Text("Enter a valid UPC Code!", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
            )
        }
    }
}
