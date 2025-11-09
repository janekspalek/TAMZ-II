package com.example.cv_08.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog (
        onDismissRequest = onDismiss,

        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button (
                onClick = onDismiss
            ) {
                Text("Confirm")
            }
        }
    )
}