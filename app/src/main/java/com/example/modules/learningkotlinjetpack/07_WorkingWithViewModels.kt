package com.example.modules.learningkotlinjetpack

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Exercise 7: Working with ViewModels
 * 
 * This file demonstrates the MVVM architecture pattern with Jetpack Compose,
 * managing reactive data streams and sharing state.
 */

/**
 * UI State data class for the feature.
 */
data class MyFeatureUiState(
    val counter: Int = 0,
    val items: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for managing feature state and operations.
 */
class MyFeatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyFeatureUiState())
    val uiState: StateFlow<MyFeatureUiState> = _uiState.asStateFlow()
    
    init {
        loadInitialData()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                // Simulate network delay
                delay(1000)
                
                // Randomly throw an exception to demonstrate error handling
                if (Random.nextBoolean()) {
                    throw Exception("Failed to load data")
                }
                
                _uiState.value = _uiState.value.copy(
                    items = listOf("Item 1", "Item 2", "Item 3"),
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "An error occurred"
                )
            }
        }
    }
    
    fun incrementCounter() {
        _uiState.value = _uiState.value.copy(
            counter = _uiState.value.counter + 1
        )
    }
    
    fun resetCounter() {
        _uiState.value = _uiState.value.copy(
            counter = 0
        )
    }
    
    fun retryLoading() {
        loadInitialData()
    }
}

/**
 * Main screen composable that demonstrates ViewModel usage.
 */
@Composable
fun MyFeatureScreen() {
    val viewModel = remember { MyFeatureViewModel() }
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Counter section
        CounterSection(
            counter = uiState.counter,
            onIncrement = { viewModel.incrementCounter() },
            onReset = { viewModel.resetCounter() }
        )

        HorizontalDivider()

        // Items section
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            ItemsSection(
                items = uiState.items,
                errorMessage = uiState.errorMessage,
                onRetry = { viewModel.retryLoading() }
            )
        }
    }
}

/**
 * Composable for displaying and managing the counter.
 */
@Composable
fun CounterSection(
    counter: Int,
    onIncrement: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Counter: $counter",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onIncrement) {
                Text("Increment")
            }
            
            Button(onClick = onReset) {
                Text("Reset")
            }
        }
    }
}

/**
 * Composable for displaying the list of items.
 */
@Composable
fun ItemsSection(
    items: List<String>,
    errorMessage: String?,
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Button(onClick = onRetry) {
                Text("Retry")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = item,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Preview for the MyFeatureScreen composable.
 */
@Preview(
    name = "My Feature Screen Preview",
    showBackground = true
)
@Composable
fun MyFeatureScreenPreview() {
    MyFeatureScreen()
}

/**
 * Preview for the CounterSection composable.
 */
@Preview(
    name = "Counter Section Preview",
    showBackground = true
)
@Composable
fun CounterSectionPreview() {
    CounterSection(
        counter = 5,
        onIncrement = {},
        onReset = {}
    )
}

/**
 * Preview for the ItemsSection composable.
 */
@Preview(
    name = "Items Section Preview",
    showBackground = true
)
@Composable
fun ItemsSectionPreview() {
    ItemsSection(
        items = listOf("Item 1", "Item 2", "Item 3"),
        errorMessage = null,
        onRetry = {}
    )
} 