package com.example.recipesearch.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.recipesearch.ui.viewmodels.SharedViewModel

@Composable
fun SavedRecipesScreen(
    viewModel: SharedViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.fetchRecipes(
            getApiResult = false,
            getDbResult = true
        )
    }

    val queryState by viewModel.queryState.observeAsState()

    when (queryState) {
        SharedViewModel.QueryState.LOADING -> {
            LoadingScreen()
        }
        SharedViewModel.QueryState.SUCCESS -> {
            RecipesList(
                viewModel = viewModel,
                navController = navController,
                useSavedRecipes = true
            )
        }
        else -> {
            Text(text = "There was an error with retrieving your saved recipes. " +
                    "Please report this to the developer.")
        }
    }
}