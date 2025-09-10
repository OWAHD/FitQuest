package com.example.fitquest.ui.screens.hydration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitquest.ui.AppViewModelProvider

//@Composable
//fun HydrationTrackerScreen(
//    viewModel: HydrationViewModel  = viewModel(factory = AppViewModelProvider.Factory)
//) {
//
//    val todayTotal by viewModel.todayTotal.collectAsState()
//    var showDialog by remember { mutableStateOf(false) }
//    var customAmount by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Hydration Tracker", style = MaterialTheme.typography.headlineSmall)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        LinearProgressIndicator(
//        progress = { viewModel.progress() },
//        modifier = Modifier
//                        .fillMaxWidth()
//                        .height(12.dp),
//        color = MaterialTheme.colorScheme.primary,
//        trackColor = ProgressIndicatorDefaults.linearTrackColor,
//        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(text = "$todayTotal ml / ${viewModel.dailyGoal} ml")
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Quick add buttons
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            listOf(150, 250, 500).forEach { amount ->
//                Button(onClick = { viewModel.addWater(amount) }) {
//                    Text("+${amount}ml")
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // Custom amount button
//        OutlinedButton(onClick = { showDialog = true }) {
//            Text("Custom Amount")
//        }
//    }
//
//    // Custom input dialog
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            confirmButton = {
//                TextButton(onClick = {
//                    val amount = customAmount.toIntOrNull()
//                    if (amount != null && amount > 0) {
//                        viewModel.addWater(amount)
//                    }
//                    showDialog = false
//                    customAmount = ""
//                }) {
//                    Text("Add")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDialog = false }) {
//                    Text("Cancel")
//                }
//            },
//            title = { Text("Enter Custom Amount") },
//            text = {
//                OutlinedTextField(
//                    value = customAmount,
//                    onValueChange = { customAmount = it },
//                    label = { Text("Amount (ml)") },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                )
//            }
//        )
//    }
//}



@Composable
fun HydrationTrackerScreen(
//    viewModel: HydrationViewModel = viewModel(factory = AppViewModelProvider.Factory),

) {
    val viewModel: HydrationViewModel = viewModel()

    val todayTotal by viewModel.todayTotal.collectAsState(0)
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var customAmount by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hydration Tracker", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Progress indicator (0f..1f range expected)
        LinearProgressIndicator(
            progress = viewModel.progress(), // just call it, no {}
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )


        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "$todayTotal ml / ${viewModel.dailyGoal} ml")

        Spacer(modifier = Modifier.height(16.dp))

        // Quick add buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(150, 250, 500).forEach { amount ->
                Button(onClick = { viewModel.addWater(amount) }) {
                    Text("+${amount}ml")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Custom amount button
        OutlinedButton(onClick = { showDialog = true }) {
            Text("Custom Amount")
        }
    }

    // Custom input dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val amount = customAmount.toIntOrNull()
                    if (amount != null && amount > 0) {
                        viewModel.addWater(amount)
                    }
                    showDialog = false
                    customAmount = ""
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Enter Custom Amount") },
            text = {
                OutlinedTextField(
                    value = customAmount,
                    onValueChange = { customAmount = it },
                    label = { Text("Amount (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        )
    }
}
