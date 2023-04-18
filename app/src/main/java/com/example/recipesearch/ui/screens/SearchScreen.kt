package com.example.recipesearch.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.recipesearch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    query: String
) {
//    Scaffold(
//        topBar = { SearchScreenTopBar() }
//    ) { innerPadding ->
//        Box(modifier = Modifier.padding(innerPadding)) {
//            SearchScreenContent(
//                navController = navController,
//                query = query
//            )
//        }
//    }
    SearchScreenContent(
        navController = navController,
        query = query
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenTopBar() {
//    TopAppBar(
//        title = {
//            Text(text = stringResource(R.string.search_screen_title))
//        }
//        navigationIcon = { BackButton() }
//    )
}

@Composable
fun SearchScreenContent(navController: NavController, query: String) {
    Text(
        text = query,
        style = MaterialTheme.typography.titleLarge
    )
}