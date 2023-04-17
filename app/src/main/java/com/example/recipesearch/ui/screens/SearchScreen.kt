package com.example.recipesearch.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SearchScreen(
    navController: NavController,
    query: String
) {
    Text(
        text = query
    )
}