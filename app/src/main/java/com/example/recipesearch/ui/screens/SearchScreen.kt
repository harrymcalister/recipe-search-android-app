package com.example.recipesearch.ui.screens

import android.util.Log
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipesearch.repositories.MainRepositoryImpl
import com.example.recipesearch.ui.viewmodels.SharedViewModel
import com.example.recipesearch.ui.viewmodels.SharedViewModelFactory

@Composable
fun SearchScreen(
    navController: NavController,
    query: String
) {
    val viewModel: SharedViewModel = viewModel(
        factory = SharedViewModelFactory(MainRepositoryImpl)
    )

    LaunchedEffect(Unit) {
        Log.e("SearchScreen.kt", "launchedEffect initiated")
        viewModel.fetchRecipes(query)
    }

    Text(
        text = query,
        style = MaterialTheme.typography.titleLarge
    )
}