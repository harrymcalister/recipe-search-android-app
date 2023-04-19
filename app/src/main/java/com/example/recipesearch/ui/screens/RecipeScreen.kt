package com.example.recipesearch.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun RecipeScreen(navController: NavController) {
    Text(
        text = "Recipe screen",
        style = MaterialTheme.typography.titleLarge
    )
}