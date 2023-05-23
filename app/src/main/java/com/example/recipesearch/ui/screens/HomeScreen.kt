package com.example.recipesearch.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipesearch.navigation.Route
import com.example.recipesearch.ui.viewmodels.SharedViewModel

@Composable
fun HomeScreen(
    viewModel: SharedViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.clearRecipes()
    }

    val searchQuery = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val submitQuery: () -> Unit = {
        if (searchQuery.value.isNotBlank()) {
            focusManager.clearFocus()
            navController.navigate(Route.SearchScreenRoute.withArgs(searchQuery.value)) {
                launchSingleTop = true
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { focusManager.clearFocus() }
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Home Screen",
                style = MaterialTheme.typography.titleLarge
            )
            SearchBar(
                searchQuery = searchQuery,
                onSubmit = submitQuery
            )
            Button(
                onClick = submitQuery
            ) {
                Text(text = "Submit")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: MutableState<String>, onSubmit: () -> Unit) {
    Row(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier,
            placeholder = { Text(text = "Search for a recipe") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
            singleLine = true,
            trailingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        )
    }
}