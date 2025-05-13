package com.example.modules.learningkotlinjetpack

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Exercise 2: Building UI Components
 * 
 * This file demonstrates the use of foundational composables and modifiers
 * to construct user interfaces.
 */

/**
 * A simple button that logs a message when clicked.
 */
@Composable
fun LoggingButton() {
    Button(
        onClick = { Log.d("UIComponents", "Button clicked!") },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Click Me!")
    }
}

/**
 * A composable that displays an image with different content scaling.
 */
@Composable
fun ScaledImage(
    imageResId: Int,
    contentDescription: String
) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Fit
    )
}

/**
 * A row of evenly spaced text elements.
 */
@Composable
fun EvenlySpacedRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("First")
        Text("Second")
        Text("Third")
    }
}

/**
 * A data class representing an item in the list.
 */
data class ListItem(
    val id: Int,
    val text: String
)

/**
 * A simple list that displays items in cards.
 */
@Composable
fun SimpleList(items: List<ListItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.text,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * The main screen that combines all components.
 */
@Composable
fun BuildingUIComponentsScreen() {
    val sampleItems = remember {
        listOf(
            ListItem(1, "Item 1"),
            ListItem(2, "Item 2"),
            ListItem(3, "Item 3"),
            ListItem(4, "Item 4"),
            ListItem(5, "Item 5")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LoggingButton()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Spacer(modifier = Modifier.height(16.dp))
        
        EvenlySpacedRow()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        SimpleList(items = sampleItems)
    }
}

/**
 * Preview for the LoggingButton composable.
 */
@Preview(
    name = "Logging Button Preview",
    showBackground = true
)
@Composable
fun LoggingButtonPreview() {
    LoggingButton()
}



/**
 * Preview for the EvenlySpacedRow composable.
 */
@Preview(
    name = "Evenly Spaced Row Preview",
    showBackground = true,
    widthDp = 300
)
@Composable
fun EvenlySpacedRowPreview() {
    EvenlySpacedRow()
}

/**
 * Preview for the SimpleList composable.
 */
@Preview(
    name = "Simple List Preview",
    showBackground = true,
    heightDp = 400
)
@Composable
fun SimpleListPreview() {
    val sampleItems = listOf(
        ListItem(1, "Item 1"),
        ListItem(2, "Item 2"),
        ListItem(3, "Item 3")
    )
    SimpleList(items = sampleItems)
}

/**
 * Preview for the complete BuildingUIComponentsScreen.
 */
@Preview(
    name = "UI Components Screen Preview",
    showBackground = true,
    heightDp = 800
)
@Composable
fun BuildingUIComponentsScreenPreview() {
    BuildingUIComponentsScreen()
} 