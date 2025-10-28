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
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.uetontop.ui.library.VideoPlayerScreen
import com.example.uetontop.ui.library.YouTubeScreen

sealed class Screen(val route: String) {
    object Onboarding1 : Screen("onboarding1")
    object Onboarding2 : Screen("onboarding2")
    object Home : Screen("Trang chủ")
    object Test : Screen("Kiểm tra")
    object Library : Screen("Thư viện")
    object Profile : Screen("Cá nhân")
    object Chat : Screen("chat")
    object Schedule : Screen("schedule")
    object VideoPlayer : Screen("video?url={url}") {
        fun routeWithUrl(url: String): String =
            "video?url=${Uri.encode(url)}"
    }

    object YouTube : Screen("youtube?vid={vid}") {
        fun routeWithId(id: String) = "youtube?vid=$id"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            VideoPlayerScreen(videoUrl = url, onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.YouTube.route,
            arguments = listOf(navArgument("vid") { type = NavType.StringType })
        ) { backStackEntry ->
            val vid = backStackEntry.arguments?.getString("vid").orEmpty()
            YouTubeScreen(videoId = vid, onBack = { navController.popBackStack() })
        }
    }
}
