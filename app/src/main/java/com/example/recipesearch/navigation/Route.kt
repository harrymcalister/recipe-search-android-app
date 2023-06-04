package com.example.recipesearch.navigation

import android.util.Log
import androidx.compose.ui.res.stringResource
import com.example.recipesearch.R

sealed class Route(val route: String, val title: String) {
    object HomeScreenRoute : Route(
        route = "home",
        title = "Search"
    )
    object SearchScreenRoute : Route(
        route = "search",
        title = "Search Results"
    )
    object RecipeScreenRoute : Route(
        route = "recipe",
        title = "Recipe Details"
    )
    object SavedRecipesScreenRoute : Route(
        route = "saved",
        title = "Saved Recipes"
    )
    object SettingsScreenRoute : Route(
        route = "settings",
        title = "Settings"
    )

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
