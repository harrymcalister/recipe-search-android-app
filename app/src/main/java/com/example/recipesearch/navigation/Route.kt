package com.example.recipesearch.navigation

import android.util.Log

sealed class Route(val route: String) {
    object HomeScreenRoute : Route(route = "home")
    object SearchScreenRoute : Route(route = "search")
    object RecipeScreenRoute : Route(route = "recipe")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
