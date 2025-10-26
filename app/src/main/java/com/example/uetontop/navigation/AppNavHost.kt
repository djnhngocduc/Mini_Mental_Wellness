package com.example.uetontop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.uetontop.ui.OnboardingScreen1
import com.example.uetontop.ui.OnboardingScreen2
import com.example.uetontop.ui.home.ChatScreen
import com.example.uetontop.ui.home.HomeScreen
import com.example.uetontop.ui.home.ScheduleScreen
import com.example.uetontop.ui.library.LibraryScreen
import com.example.uetontop.ui.profile.ProfileScreen
import com.example.uetontop.ui.test.TestScreen

sealed class Screen(val route: String) {
    object Onboarding1 : Screen("onboarding1")
    object Onboarding2 : Screen("onboarding2")
    object Home : Screen("home")
    object Test : Screen("test")
    object Library : Screen("library")
    object Profile : Screen("profile")
    object Chat : Screen("chat")
    object Schedule : Screen("schedule")
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Onboarding1.route) {
        composable(Screen.Onboarding1.route) { OnboardingScreen1(navController) }
        composable(Screen.Onboarding2.route) { OnboardingScreen2(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Test.route) { TestScreen(navController) }
        composable(Screen.Library.route) { LibraryScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Chat.route) { ChatScreen(navController) }
        composable(Screen.Schedule.route) { ScheduleScreen(navController) }

    }
}
