package com.example.modules.learningkotlinjetpack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Exercise 1: Basic Composable Functions
 * 
 * This file demonstrates the fundamental principles of Jetpack Compose through
 * simple composable functions and previews.
 */

/**
 * A basic composable that displays a greeting message.
 * 
 * @param name The name to greet
 * @param greetingMessage The custom greeting message to display
 */
@Composable
fun GreetingText(
    name: String,
    greetingMessage: String = "Hello"
) {
    Text(
        text = "$greetingMessage $name",
        style = MaterialTheme.typography.headlineMedium
    )
}

/**
 * A composable that displays a farewell message.
 * 
 * @param name The name to bid farewell to
 */
@Composable
fun LearningText(name: String) {
    Text(
        text = "$name, you are learning Jetpack Compose!",
        style = MaterialTheme.typography.headlineMedium
    )
}

/**
 * The main screen composable that combines both greeting and farewell messages.
 * 
 * @param name The name to use in both messages
 * @param greetingMessage Optional custom greeting message
 */
@Composable
fun BasicsMainScreen(
    name: String,
    greetingMessage: String = "Hello"
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        GreetingText(
            name = name,
            greetingMessage = greetingMessage
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LearningText(name = name)
    }
}

/**
 * Preview for the GreetingText composable with default greeting.
 */
@Preview(
    name = "Greeting Text Preview",
    showBackground = true,
    widthDp = 300
)
@Composable
fun GreetingTextPreview() {
    GreetingText(name = "Android")
}

/**
 * Preview for the GreetingText composable with custom greeting.
 */
@Preview(
    name = "Custom Greeting Text Preview",
    showBackground = true,
    widthDp = 300
)
@Composable
fun CustomGreetingTextPreview() {
    GreetingText(
        name = "Android",
        greetingMessage = "Welcome"
    )
}

/**
 * Preview for the LearningText composable.
 */
@Preview(
    name = "Farewell Text Preview",
    showBackground = true,
    widthDp = 300
)
@Composable
fun LearningTextPreview() {
    LearningText(name = "Android")
}

/**
 * Preview for the complete BasicsMainScreen composable.
 */
@Preview(
    name = "Main Screen Preview",
    showBackground = true,
    widthDp = 300,
    heightDp = 200
)
@Composable
fun BasicsMainScreenPreview() {
    BasicsMainScreen(
        name = "Android",
        greetingMessage = "Welcome"
    )
}

/**
 * Preview for the BasicsMainScreen composable in dark mode.
 */
@Preview(
    name = "Main Screen Dark Preview",
    showBackground = true,
    widthDp = 300,
    heightDp = 200,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun BasicsMainScreenDarkPreview() {
    BasicsMainScreen(
        name = "Android",
        greetingMessage = "Welcome"
    )
} 