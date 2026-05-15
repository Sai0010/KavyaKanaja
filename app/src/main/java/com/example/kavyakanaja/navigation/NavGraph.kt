// navigation/NavGraph.kt

package com.example.kavyakanaja.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kavyakanaja.screens.FavouritesScreen
import com.example.kavyakanaja.screens.HomeScreen
import com.example.kavyakanaja.screens.LibraryScreen
import com.example.kavyakanaja.screens.PoemDetailScreen
import com.example.kavyakanaja.screens.PoetProfileScreen
import com.example.kavyakanaja.screens.PoetsListScreen
import com.example.kavyakanaja.screens.SplashScreen

// All screen routes defined in one place
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Library : Screen("library")
    object PoemDetail : Screen("poem_detail/{poemId}") {
        fun createRoute(poemId: Int) = "poem_detail/$poemId"
    }
    object PoetsList : Screen("poets_list")
    object PoetProfile : Screen("poet_profile/{poetId}") {
        fun createRoute(poetId: Int) = "poet_profile/$poetId"
    }
    object Search : Screen("search")
    object Favourites : Screen("favourites")
}

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Library.route) {
            LibraryScreen(navController)
        }
        composable(
            route = Screen.PoemDetail.route,
            arguments = listOf(navArgument("poemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val poemId = backStackEntry.arguments?.getInt("poemId") ?: 1
            PoemDetailScreen(navController = navController, poemId = poemId)
        }
        composable(Screen.PoetsList.route) {
            PoetsListScreen(navController)
        }
        composable(
            route = Screen.PoetProfile.route,
            arguments = listOf(navArgument("poetId") { type = NavType.IntType })
        ) { backStackEntry ->
            val poetId = backStackEntry.arguments?.getInt("poetId") ?: 1
            PoetProfileScreen(navController = navController, poetId = poetId)
        }
        composable(Screen.Search.route) {
            LibraryScreen(navController)
        }
        composable(Screen.Favourites.route) {
            FavouritesScreen(navController)
        }
    }
}