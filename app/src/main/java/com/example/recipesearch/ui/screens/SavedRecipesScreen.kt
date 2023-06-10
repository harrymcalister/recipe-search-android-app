package com.example.recipesearch.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.recipesearch.ui.viewmodels.SharedViewModel

@Composable
fun SavedRecipesScreen(
    viewModel: SharedViewModel,
    navController: NavController
) {
    RecipeList(
        viewModel = viewModel,
        navController = navController,
        useSavedRecipes = true
    )
}