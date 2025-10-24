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

    NavigationBar {
        items.forEach { screen ->
            val icon = when (screen) {
                is Screen.Home -> Icons.Default.Home
                is Screen.Test -> Icons.Default.CheckCircle
                is Screen.Library -> Icons.Default.FavoriteBorder
                is Screen.Profile -> Icons.Default.AccountCircle
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
                label = { Text(screen.route.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}
