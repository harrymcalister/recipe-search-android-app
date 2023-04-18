package com.example.recipesearch.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun RecipeScreen(navController: NavController) {
    RecipeScreenContent(navController = navController)
}

@Composable
fun RecipeScreenContent(navController: NavController) {
    Text(
        text = "Recipe screen"
    )
}