package com.example.modules.learningkotlinjetpack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun PerformanceScreen() {
    var inputValue by remember { mutableStateOf("") }
    var items by remember { mutableStateOf(List(100) { "Item $it" }) }
    var showExpensiveCalculation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. State Hoisting Example
        Text("State Hoisting Example")
        StateHoistingExample(
            value = inputValue,
            onValueChange = { inputValue = it }
        )

        // 2. Derived State Example
        Text("Derived State Example")
        val derivedValue = remember(inputValue) {
            derivedStateOf {
                // Expensive calculation that only runs when inputValue changes
                inputValue.reversed()
            }
        }
        Text("Reversed: ${derivedValue.value}")

        // 3. LazyColumn with Keys Example
        Text("LazyColumn with Keys Example")
        Button(
            onClick = { items = items.shuffled() },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Shuffle Items")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(
                items = items,
                key = { it } // Using the item itself as the key
            ) { item ->
                ListItem(item = item)
            }
        }

        // 4. Deferred State Reading Example
        Text("Deferred State Reading Example")
        Button(
            onClick = { showExpensiveCalculation = !showExpensiveCalculation }
        ) {
            Text("Toggle Expensive Calculation")
        }

        if (showExpensiveCalculation) {
            ExpensiveCalculationResult(inputValue)
        }
    }
}

@Composable
private fun StateHoistingExample(
    value: String,
    onValueChange: (String) -> Unit
) {
    // State is hoisted to parent, making this composable stateless and more reusable
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ListItem(item: String) {
    // Using remember to avoid recreating the color on every recomposition
    val backgroundColor = remember(item) {
        Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 0.1f
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(item)
    }
}

@Composable
private fun ExpensiveCalculationResult(input: String) {
    // Using LaunchedEffect to perform expensive calculation in a coroutine
    var result by remember { mutableStateOf("") }
    
    LaunchedEffect(input) {
        // Simulate expensive calculation
        delay(1000)
        result = "Processed: $input"
    }

    Text(result)
}

// Preview
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PerformanceScreenPreview() {
    MaterialTheme {
        PerformanceScreen()
    }
} 