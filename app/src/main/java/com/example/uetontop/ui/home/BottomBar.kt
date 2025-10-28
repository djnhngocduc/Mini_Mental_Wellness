package com.example.uetontop.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.uetontop.navigation.Screen

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(Screen.Home, Screen.Test, Screen.Library, Screen.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Ẩn BottomBar ở các màn không cần
    val shouldShowBottomBar = currentRoute !in listOf(
        Screen.Onboarding1.route,
        Screen.Onboarding2.route,
        Screen.Chat.route
    )

    if (shouldShowBottomBar) {
        NavigationBar {
            items.forEach { screen ->
                val icon = when (screen.route) {
                    Screen.Home.route -> Icons.Default.Home
                    Screen.Test.route -> Icons.Default.CheckCircle
                    Screen.Library.route -> Icons.Default.FavoriteBorder
                    Screen.Profile.route -> Icons.Default.AccountCircle
                    else -> Icons.Default.Home
                }

                NavigationBarItem(
                    selected = currentRoute == screen.route,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = { Icon(icon, contentDescription = screen.route) },
                    label = { Text(screen.route) }
                )
            }
        }
    }
}

