package com.example.recipesearch.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipesearch.navigation.Route

@Composable
fun HomeScreen(navController: NavController) {
    val searchQuery = remember { mutableStateOf("") }

    fun submitQuery() {
        navController.navigate(Route.SearchScreenRoute.withArgs(searchQuery.value)) {
            launchSingleTop = true
        }
    }
    Column {
        Text(text = "Home Screen")
        SearchBar(
            searchQuery = searchQuery,
            onSubmit = { submitQuery() }
        )
        Button(
            onClick = { submitQuery() }
        ) {
            Text(text = "Submit")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: MutableState<String>, onSubmit: () -> Unit) {
    Row(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { newValue -> searchQuery.value = newValue },
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
