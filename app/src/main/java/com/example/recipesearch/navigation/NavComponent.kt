package com.example.recipesearch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipesearch.ui.screens.HomeScreen
import com.example.recipesearch.ui.screens.SearchScreen

@Composable
fun NavComponent() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.HomeScreenRoute.route
    ) {
        composable(route = Route.HomeScreenRoute.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Route.SearchScreenRoute.route + "/{query}",
            arguments = listOf(
                navArgument("query") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            SearchScreen(
                navController = navController,
                query = backStackEntry.arguments!!.getString("query")!!
            )
        }
    }
}