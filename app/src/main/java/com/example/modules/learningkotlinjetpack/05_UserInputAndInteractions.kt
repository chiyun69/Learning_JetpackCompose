package com.example.modules.learningkotlinjetpack

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * Exercise 5: User Input and Interactions
 * 
 * This file demonstrates handling user input events and managing state
 * effectively within Compose applications.
 */

/**
 * A screen that demonstrates various user input components and state management.
 */
@Composable
fun UserInputScreen() {
    var text by remember { mutableStateOf("") }
    var count by remember { mutableStateOf(0) }
    var isChecked by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Text input with keyboard actions
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter text") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Log search action
                    println("Search triggered with text: $text")
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Counter with increment/decrement buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { if (count > 0) count-- },
                enabled = count > 0
            ) {
                Text("-")
            }
            
            Text(
                text = "Count: $count",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Button(
                onClick = { count++ }
            ) {
                Text("+")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Checkbox with conditional text color
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            
            Text(
                text = "Agree to terms",
                color = if (isChecked) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

/**
 * A composable that demonstrates gesture handling.
 */
@Composable
fun GestureDemo() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
                .size(100.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("Drag me!")
            }
        }
    }
}

/**
 * A composable that demonstrates form validation.
 */
@Composable
fun FormValidationDemo() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                isEmailValid = it.contains("@")
            },
            label = { Text("Email") },
            isError = !isEmailValid,
            supportingText = {
                if (!isEmailValid) {
                    Text("Please enter a valid email address")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                isPasswordValid = it.length >= 6
            },
            label = { Text("Password") },
            isError = !isPasswordValid,
            supportingText = {
                if (!isPasswordValid) {
                    Text("Password must be at least 6 characters")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        Button(
            onClick = { /* Handle form submission */ },
            enabled = isEmailValid && isPasswordValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}

/**
 * Preview for the UserInputScreen composable.
 */
@Preview(
    name = "User Input Screen Preview",
    showBackground = true
)
@Composable
fun UserInputScreenPreview() {
    UserInputScreen()
}

/**
 * Preview for the GestureDemo composable.
 */
@Preview(
    name = "Gesture Demo Preview",
    showBackground = true
)
@Composable
fun GestureDemoPreview() {
    GestureDemo()
}

/**
 * Preview for the FormValidationDemo composable.
 */
@Preview(
    name = "Form Validation Demo Preview",
    showBackground = true
)
@Composable
fun FormValidationDemoPreview() {
    FormValidationDemo()
} 