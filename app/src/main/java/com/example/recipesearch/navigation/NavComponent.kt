package com.example.recipesearch.navigation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipesearch.R
import com.example.recipesearch.repositories.MainRepositoryImpl
import com.example.recipesearch.ui.screens.HomeScreen
import com.example.recipesearch.ui.screens.RecipeScreen
import com.example.recipesearch.ui.screens.SearchScreen
import com.example.recipesearch.ui.viewmodels.SharedViewModel
import com.example.recipesearch.ui.viewmodels.SharedViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavComponent() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel(
        factory = SharedViewModelFactory(repository = MainRepositoryImpl)
    )
    Scaffold(
        topBar = { MyTopBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Route.HomeScreenRoute.route
        ) {
            composable(route = Route.HomeScreenRoute.route) {
                HomeScreen(
                    viewModel = sharedViewModel,
                    navController = navController
                )
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
                    viewModel = sharedViewModel,
                    navController = navController,
                    query = backStackEntry.arguments!!.getString("query")!!
                )
            }
            composable(
                route = Route.RecipeScreenRoute.route + "/{recipesIndex}",
                arguments = listOf(
                    navArgument("recipesIndex") {
                        type = NavType.IntType
                        nullable = false
                    }
                )
            ) { backStackEntry ->
                RecipeScreen(
                    viewModel = sharedViewModel,
                    navController = navController,
                    recipesIndex = backStackEntry.arguments!!.getInt("recipesIndex")
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isNotOnHomePage = when (navBackStackEntry?.destination?.route) {
        null, "home" -> false
        else -> true
    }
    if (isNotOnHomePage) {
        TopAppBar(
            title = {
                Text(text = stringResource(getAppBarTitleResId(navBackStackEntry)))
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
    }
}