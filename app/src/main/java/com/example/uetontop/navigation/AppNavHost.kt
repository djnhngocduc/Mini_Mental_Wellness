package com.example.uetontop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uetontop.ui.home.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
//    object Test : Screen("test")
//    object Library : Screen("library")
//    object Profile : Screen("profile")
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
//        composable(Screen.Test.route) { TestScreen() }
//        composable(Screen.Library.route) { LibraryScreen() }
//        composable(Screen.Profile.route) { ProfileScreen() }
    }
}
