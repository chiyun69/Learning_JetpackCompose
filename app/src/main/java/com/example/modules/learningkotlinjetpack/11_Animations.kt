package com.example.modules.learningkotlinjetpack

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimationsScreen() {
    var isVisible by remember { mutableStateOf(true) }
    var isExpanded by remember { mutableStateOf(false) }
    var currentPage by remember { mutableStateOf(0) }
    var isPressed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Basic value animation with spring
        val scale by animateFloatAsState(
            targetValue = if (isPressed) 1.2f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "scale"
        )

        Button(
            onClick = { isPressed = !isPressed },
            modifier = Modifier.scale(scale)
        ) {
            Text("Spring Animation")
        }

        // 2. AnimatedVisibility with custom transitions
        Button(onClick = { isVisible = !isVisible }) {
            Text("Toggle Visibility")
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { -it }
            ) + expandVertically() + fadeIn(),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

        // 3. Crossfade between different content
        Button(onClick = { currentPage = (currentPage + 1) % 2 }) {
            Text("Switch Content")
        }

        Crossfade(
            targetState = currentPage,
            animationSpec = tween(500)
        ) { page ->
            when (page) {
                0 -> Text("First Page", fontSize = 24.sp)
                1 -> Text("Second Page", fontSize = 24.sp)
            }
        }

        // 4. Multiple animations with updateTransition
        val transition = updateTransition(isExpanded, label = "expand")
        val size by transition.animateDp(
            transitionSpec = {
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            },
            label = "size"
        ) { expanded ->
            if (expanded) 200.dp else 100.dp
        }

        val color by transition.animateColor(
            transitionSpec = {
                tween(500)
            },
            label = "color"
        ) { expanded ->
            if (expanded) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
        }

        Button(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Animate Size and Color")
        }

        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(color)
        )

        // 5. Infinite animation
        val infiniteTransition = rememberInfiniteTransition(label = "infinite")
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation"
        )

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(MaterialTheme.colorScheme.tertiary)
                .graphicsLayer {
                    rotationZ = rotation
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnimationsScreenPreview() {
    MaterialTheme {
        AnimationsScreen()
    }
} 