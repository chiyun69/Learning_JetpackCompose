package com.example.modules.learningkotlinjetpack

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * Exercise 4: Navigation and Routing
 * 
 * This file demonstrates navigation between different screens within a Compose application.
 */

/**
 * Navigation routes for the app.
 */
object Routes {
    const val HOME = "home"
    const val DETAILS = "details/{itemId}"
    const val PROFILE = "profile"
    
    fun detailsRoute(itemId: Int) = "details/$itemId"
}

/**
 * Home screen composable.
 */
@Composable
fun NavigationHomeScreen(
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Home Screen",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { onNavigateToDetails(1) }
        ) {
            Text("Go to Details")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = onNavigateToProfile
        ) {
            Text("Go to Profile")
        }
    }
}

/**
 * Details screen composable.
 */
@Composable
fun DetailsScreen(
    itemId: Int,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Details Screen",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Item ID: $itemId",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onNavigateBack) {
            Text("Go Back")
        }
    }
}

/**
 * Profile screen composable.
 */
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Profile Screen",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "This is your profile page",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onNavigateBack) {
            Text("Go Back")
        }
    }
}

/**
 * Main navigation host composable.
 */
@Composable
fun AppNavigationHost() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            NavigationHomeScreen(
                onNavigateToDetails = { itemId ->
                    navController.navigate(Routes.detailsRoute(itemId))
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }
        
        composable(
            route = Routes.DETAILS,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            DetailsScreen(
                itemId = itemId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Routes.PROFILE) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Preview for the HomeScreen composable.
 */
@Preview(
    name = "Home Screen Preview",
    showBackground = true
)
@Composable
fun NavigationHomeScreenPreview() {
    NavigationHomeScreen(
        onNavigateToDetails = {},
        onNavigateToProfile = {}
    )
}

/**
 * Preview for the DetailsScreen composable.
 */
@Preview(
    name = "Details Screen Preview",
    showBackground = true
)
@Composable
fun DetailsScreenPreview() {
    DetailsScreen(
        itemId = 1,
        onNavigateBack = {}
    )
}

/**
 * Preview for the ProfileScreen composable.
 */
@Preview(
    name = "Profile Screen Preview",
    showBackground = true
)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        onNavigateBack = {}
    )
} 