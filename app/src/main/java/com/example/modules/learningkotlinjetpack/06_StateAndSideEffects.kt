package com.example.modules.learningkotlinjetpack

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Exercise 6: State and Side-Effects
 * 
 * This file demonstrates state management techniques and handling
 * operations that occur outside the scope of composable functions.
 */

/**
 * ViewModel for managing timer state and operations.
 */
class TimerViewModel : ViewModel() {
    private val _elapsedTime = MutableStateFlow(0)
    val elapsedTime: StateFlow<Int> = _elapsedTime.asStateFlow()
    
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()
    
    init {
        startTimer()
    }
    
    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_isRunning.value) {
                    _elapsedTime.value++
                }
            }
        }
    }
    
    fun toggleTimer() {
        _isRunning.value = !_isRunning.value
    }
    
    fun resetTimer() {
        _elapsedTime.value = 0
    }
}

/**
 * A screen that demonstrates state management and side effects.
 */
@Composable
fun StateAndSideEffectsScreen() {
    var dataLoaded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Timer section
            TimerSection()
            
            HorizontalDivider()
            
            // Data loading section
            Button(
                onClick = {
                    coroutineScope.launch {
                        dataLoaded = false
                        delay(2000) // Simulate data loading
                        dataLoaded = true
                    }
                }
            ) {
                Text("Load Data")
            }
            
            if (dataLoaded) {
                Text(
                    text = "Data Loaded!",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            // Lifecycle observer section
            LifecycleObserverDemo()
        }
    }
}

/**
 * A composable that demonstrates timer functionality using ViewModel.
 */
@Composable
fun TimerSection() {
    val viewModel = remember { TimerViewModel() }
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // LaunchedEffect to show snackbar when timer reaches 10
    LaunchedEffect(elapsedTime) {
        if (elapsedTime == 10) {
            snackbarHostState.showSnackbar("Timer reached 10 seconds!")
            viewModel.resetTimer()
        }
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Timer: $elapsedTime seconds",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.toggleTimer() }
            ) {
                Text(if (isRunning) "Pause" else "Start")
            }
            
            Button(
                onClick = { viewModel.resetTimer() }
            ) {
                Text("Reset")
            }
        }
    }
}

/**
 * A composable that demonstrates lifecycle observation.
 */
@Composable
fun LifecycleObserverDemo() {
    var lifecycleState by remember { mutableStateOf("") }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycleState = when (event) {
                Lifecycle.Event.ON_CREATE -> "Created"
                Lifecycle.Event.ON_START -> "Started"
                Lifecycle.Event.ON_RESUME -> "Resumed"
                Lifecycle.Event.ON_PAUSE -> "Paused"
                Lifecycle.Event.ON_STOP -> "Stopped"
                Lifecycle.Event.ON_DESTROY -> "Destroyed"
                else -> lifecycleState
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Lifecycle State:",
            style = MaterialTheme.typography.titleMedium
        )
        
        Text(
            text = lifecycleState,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Preview for the StateAndSideEffectsScreen composable.
 */
@Preview(
    name = "State and Side Effects Screen Preview",
    showBackground = true
)
@Composable
fun StateAndSideEffectsScreenPreview() {
    StateAndSideEffectsScreen()
}

/**
 * Preview for the TimerSection composable.
 */
@Preview(
    name = "Timer Section Preview",
    showBackground = true
)
@Composable
fun TimerSectionPreview() {
    TimerSection()
}

/**
 * Preview for the LifecycleObserverDemo composable.
 */
@Preview(
    name = "Lifecycle Observer Demo Preview",
    showBackground = true
)
@Composable
fun LifecycleObserverDemoPreview() {
    LifecycleObserverDemo()
} 