package com.example.recipesearch.ui.screens

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipesearch.repositories.MainRepositoryImpl
import com.example.recipesearch.ui.viewmodels.SharedViewModel
import com.example.recipesearch.ui.viewmodels.SharedViewModelFactory
import org.w3c.dom.Text

@Composable
fun RecipeScreen(
    viewModel: SharedViewModel,
    navController: NavController,
    recipesIndex: Int
) {
    val selectedRecipe = viewModel.recipes.value!!.results[recipesIndex]

    Text(
        text = "Selected recipe: ${selectedRecipe.name}",
        style = MaterialTheme.typography.titleLarge
    )
}