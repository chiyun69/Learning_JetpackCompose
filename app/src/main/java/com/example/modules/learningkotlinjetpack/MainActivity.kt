package com.example.modules.learningkotlinjetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.modules.learningkotlinjetpack.ui.theme.LearningKotlinJetpackTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningKotlinJetpackTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableIntStateOf(0) }

    val menuItems = listOf(
        "Basics of Jetpack Compose" to "01_Basics",
        "Building UI Components" to "02_UIComponents",
        "Layouts - ConstraintLayout" to "03_Layouts_ComplexLayoutWithConstraintLayout",
        "Layouts - Scaffold with Bottom Bar" to "03_Layouts_ScaffoldWithBottomBar",
        "Custom Theme" to "03_Layouts_CustomTheme",
        "Navigation and Routing" to "04_Navigation",
        "User Input" to "05_UserInput",
        "User Gestures" to "05_Gestures",
        "Form Validation" to "05_FormValidation",
        "State and Side-Effects" to "06_StateAndSideEffects",
        "Working with ViewModels" to "07_ViewModels",
        "API Requests" to "08_ApiRequests",
        "Animations and Motion" to "11_Animations",
        "Performance Optimization" to "13_Performance",
        "Advanced Topics" to "14_AdvancedTopics"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Learning Kotlin Jetpack",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                HorizontalDivider()
                menuItems.forEachIndexed { index, (title, route) ->
                    NavigationDrawerItem(
                        label = { Text(title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Learning Kotlin Jetpack") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "01_Basics",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("01_Basics") { BasicsScreen() }
                composable("02_UIComponents") { BuildingUIComponentsScreen() }
                composable("03_Layouts_ComplexLayoutWithConstraintLayout") { ComplexLayoutWithConstraintLayout() }
                composable("03_Layouts_CustomTheme") { CustomThemeScreen() }
                composable("03_Layouts_ScaffoldWithBottomBar") { ScaffoldWithBottomBar() }
                composable("04_Navigation") { AppNavigationHost() }
                composable("05_UserInput") { UserInputScreen() }
                composable("05_Gestures") { GestureDemo() }
                composable("05_FormValidation") { FormValidationDemo() }
                composable("06_StateAndSideEffects") { StateAndSideEffectsScreen() }
                composable("07_ViewModels") { MyFeatureScreen() }
                composable("08_ApiRequests") { ApiScreen() }
                composable("11_Animations") { AnimationsScreen() }
                composable("13_Performance") { PerformanceScreen() }
                composable("14_AdvancedTopics") { AdvancedTopicsScreen() }
            }
        }
    }
}

@Composable
fun BasicsScreen() {
    BasicsMainScreen(
        name = "CZY",
        greetingMessage = "Welcome"
    )
}
