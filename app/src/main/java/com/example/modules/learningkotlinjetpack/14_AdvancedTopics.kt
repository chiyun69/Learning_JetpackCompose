package com.example.modules.learningkotlinjetpack

import android.Manifest
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AdvancedTopicsScreen(
    viewModel: AdvancedTopicsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Permissions Example
        Text("Permissions Example", style = MaterialTheme.typography.titleLarge)
        LocationPermissionExample()

        // 2. Custom Composable Example
        Text("Custom Rating Bar", style = MaterialTheme.typography.titleLarge)
        var rating by remember { mutableStateOf(0) }
        RatingBar(
            rating = rating,
            onRatingChanged = { rating = it }
        )

        // 3. Advanced Animation Example
        Text("Advanced Animation", style = MaterialTheme.typography.titleLarge)
        var isExpanded by remember { mutableStateOf(false) }
        Button(
            onClick = { isExpanded = !isExpanded }
        ) {
            Text("Toggle Animation")
        }
        AnimatedContent(
            targetState = isExpanded,
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn()).togetherWith(slideOutVertically { height -> -height } + fadeOut())
            }
        ) { expanded ->
            if (expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }

        // 4. WorkManager Example
        Text("WorkManager Example", style = MaterialTheme.typography.titleLarge)
        Button(
            onClick = { viewModel.scheduleWork(context) }
        ) {
            Text("Schedule Background Work")
        }
        Text("Work Status: ${uiState.workStatus}")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationPermissionExample() {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            permissionState.status.isGranted -> {
                Text("Location permission granted")
            }
            permissionState.status.shouldShowRationale -> {
                Text("Location permission is needed for this feature")
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text("Request Permission")
                }
            }
            else -> {
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text("Request Permission")
                }
            }
        }
    }
}

@Composable
private fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star ${index + 1}",
                tint = if (index < rating) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { onRatingChanged(index + 1) }
            )
        }
    }
}

@HiltViewModel
class AdvancedTopicsViewModel @Inject constructor(
    private val workManager: WorkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdvancedTopicsUiState())
    val uiState: StateFlow<AdvancedTopicsUiState> = _uiState.asStateFlow()

    fun scheduleWork(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<ExampleWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        workManager.enqueue(workRequest)

        // Observe work status
        viewModelScope.launch {
            workManager.getWorkInfoByIdFlow(workRequest.id)
                .collect { workInfo ->
                    _uiState.value = _uiState.value.copy(
                        workStatus = workInfo?.state?.name ?: "Unknown"
                    )
                }
        }
    }
}

data class AdvancedTopicsUiState(
    val workStatus: String = "Not Started"
)

class ExampleWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        // Simulate some background work
        delay(5000)
        return Result.success()
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun AdvancedTopicsScreenPreview() {
    MaterialTheme {
        AdvancedTopicsScreen()
    }
} 