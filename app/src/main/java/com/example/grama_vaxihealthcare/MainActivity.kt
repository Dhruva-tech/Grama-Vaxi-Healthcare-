package com.example.grama_vaxihealthcare

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.grama_vaxihealthcare.navigation.Screen
import com.example.grama_vaxihealthcare.ui.components.AppBackground
import com.example.grama_vaxihealthcare.ui.components.ReadableNavSurface
import com.example.grama_vaxihealthcare.ui.screens.*
import com.example.grama_vaxihealthcare.ui.theme.GramaVaxiHealthcareTheme
import com.example.grama_vaxihealthcare.ui.theme.RuralGreenDark
import com.example.grama_vaxihealthcare.viewmodel.GramaVaxiViewModel
import com.example.grama_vaxihealthcare.viewmodel.GramaVaxiViewModelFactory

class MainActivity : AppCompatActivity() {
    private val viewModel: GramaVaxiViewModel by viewModels {
        GramaVaxiViewModelFactory((application as GramaVaxiApplication).repository, applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GramaVaxiHealthcareTheme {
                AppBackground {
                    val navController = rememberNavController()
                    val currentFarmer by viewModel.farmer.collectAsState()
                    
                    val items = listOf(
                        Triple(Screen.Home, stringResource(R.string.home), Icons.Default.Home),
                        Triple(Screen.Animals, stringResource(R.string.animals), Icons.Default.Pets),
                        Triple(Screen.Calendar, stringResource(R.string.calendar), Icons.Default.CalendarMonth),
                        Triple(Screen.Inbox, stringResource(R.string.inbox), Icons.Default.Inbox),
                        Triple(Screen.Alerts, stringResource(R.string.alerts), Icons.Default.Notifications),
                        Triple(Screen.Profile, stringResource(R.string.profile), Icons.Default.Person)
                    )

                    Scaffold(
                        containerColor = Color.Transparent,
                        bottomBar = {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            
                            val mainRoutes = listOf(
                                Screen.Home.route, 
                                Screen.Animals.route, 
                                Screen.Calendar.route, 
                                Screen.Inbox.route,
                                Screen.Alerts.route, 
                                Screen.Profile.route
                            )

                            if (currentDestination?.route in mainRoutes) {
                                NavigationBar(
                                    containerColor = ReadableNavSurface,
                                    tonalElevation = 12.dp,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ) {
                                    items.forEach { (screen, label, icon) ->
                                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                        NavigationBarItem(
                                            icon = { 
                                                Icon(
                                                    imageVector = icon, 
                                                    contentDescription = label,
                                                    modifier = Modifier.size(26.dp)
                                                ) 
                                            },
                                            label = { 
                                                Text(
                                                    label, 
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                                                ) 
                                            },
                                            selected = selected,
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = RuralGreenDark,
                                                selectedTextColor = RuralGreenDark,
                                                unselectedIconColor = Color(0xFF757575),
                                                unselectedTextColor = Color(0xFF757575),
                                                indicatorColor = Color(0xFFE7F0E4)
                                            ),
                                            onClick = {
                                                navController.navigate(screen.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Splash.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Splash.route) {
                                SplashScreen {
                                    if (currentFarmer == null) {
                                        navController.navigate(Screen.Register.route) {
                                            popUpTo(Screen.Splash.route) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Splash.route) { inclusive = true }
                                        }
                                    }
                                }
                            }
                            composable(Screen.Register.route) {
                                RegistrationScreen(viewModel) {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Register.route) { inclusive = true }
                                    }
                                }
                            }
                            composable(Screen.Home.route) { HomeScreen(viewModel, navController) }
                            composable(Screen.Animals.route) { AnimalListScreen(viewModel, navController) }
                            composable(Screen.AddAnimal.route) { AddAnimalScreen(viewModel, navController) }
                            composable(
                                route = Screen.AnimalDetail.route,
                                arguments = listOf(navArgument("animalId") { type = NavType.LongType })
                            ) { backStackEntry ->
                                val animalId = backStackEntry.arguments?.getLong("animalId") ?: -1L
                                AnimalDetailScreen(animalId, viewModel, navController)
                            }
                            composable(Screen.Calendar.route) { CalendarScreen(viewModel) }
                            composable(Screen.Inbox.route) { InboxScreen(viewModel) }
                            composable(Screen.Alerts.route) { AlertsScreen(viewModel) }
                            composable(Screen.Profile.route) { 
                                ProfileScreen(viewModel, navController)
                            }
                            composable(Screen.DiseaseReport.route) { DiseaseReportScreen(viewModel, navController) }
                        }
                    }
                }
            }
        }
    }
}
