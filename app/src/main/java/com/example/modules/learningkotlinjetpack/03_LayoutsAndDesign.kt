package com.example.modules.learningkotlinjetpack

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

/**
 * Exercise 3: Layouts and Design
 * 
 * This file demonstrates advanced layout techniques and Material Design 3
 * implementation in Jetpack Compose.
 */

/**
 * A complex layout using ConstraintLayout with multiple elements.
 */
@Composable
fun ComplexLayoutWithConstraintLayout() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val (title, subtitle, image, button) = createRefs()
        
        // Title
        Text(
            text = "Welcome to Material Design 3",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        
        // Subtitle
        Text(
            text = "A modern approach to Android UI design",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(subtitle) {
                top.linkTo(title.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        
//        // Image
//        Image(
//            painter = painterResource(id = R.drawable.placeholder),
//            contentDescription = "Sample image",
//            contentScale = ContentScale.Fit,
//            modifier = Modifier
//                .size(200.dp)
//                .clip(RoundedCornerShape(8.dp))
//                .constrainAs(image) {
//                    top.linkTo(subtitle.bottom, margin = 16.dp)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                }
//        )
        
        // Button
        Button(
            onClick = { /* Handle click */ },
            modifier = Modifier.constrainAs(button) {
                top.linkTo(subtitle.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        ) {
            Text("Get Started")
        }
    }
}

/**
 * A custom theme implementation using Material Design 3.
 */
@Composable
fun CustomThemeScreen() {
    val customShapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(16.dp)
    )
    
    MaterialTheme(
        shapes = customShapes
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card with small shape
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Small Shape Card",
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            // Card with medium shape
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Medium Shape Card",
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            // Card with large shape
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = "Large Shape Card",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

/**
 * A screen using Scaffold with a BottomAppBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithBottomBar() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Search", "Profile")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top bar title") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation */ }) {
                        Icon(Icons.Default.Home, contentDescription = "Menu")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Menu, contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Selected: ${items[selectedItem]}",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

/**
 * Preview for the ComplexLayoutWithConstraintLayout composable.
 */
@Preview(
    name = "Complex Layout Preview",
    showBackground = true,
    heightDp = 600
)
@Composable
fun ComplexLayoutPreview() {
    ComplexLayoutWithConstraintLayout()
}

/**
 * Preview for the CustomThemeScreen composable.
 */
@Preview(
    name = "Custom Theme Preview",
    showBackground = true,
    heightDp = 400
)
@Composable
fun CustomThemePreview() {
    CustomThemeScreen()
}

/**
 * Preview for the ScaffoldWithBottomBar composable.
 */
@Preview(
    name = "Scaffold Preview",
    showBackground = true,
    heightDp = 800
)
@Composable
fun ScaffoldPreview() {
    ScaffoldWithBottomBar()
} 