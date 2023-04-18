package com.example.recipesearch.navigation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipesearch.R
import com.example.recipesearch.ui.screens.HomeScreen
import com.example.recipesearch.ui.screens.RecipeScreen
import com.example.recipesearch.ui.screens.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavComponent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(getAppBarTitleResId(navBackStackEntry)))
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
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
            composable(route = Route.RecipeScreenRoute.route) {
                RecipeScreen(navController = navController)
            }
        }
    }
}

private fun getAppBarTitleResId(currentBackStackEntry: NavBackStackEntry?): Int {
    val resId = when (currentBackStackEntry?.destination?.route) {
        null, "home" -> R.string.home_screen_title
        "recipe" -> R.string.recipe_screen_title
        else -> R.string.search_screen_title
    }
    return resId
}