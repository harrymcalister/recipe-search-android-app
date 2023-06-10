package com.example.recipesearch.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipesearch.model.Recipe
import com.example.recipesearch.ui.viewmodels.SharedViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.*
import coil.request.ImageRequest
import com.example.recipesearch.R
import com.example.recipesearch.navigation.Route

@Composable
fun SearchScreen(
    viewModel: SharedViewModel,
    navController: NavController,
    query: String
) {
    LaunchedEffect(Unit) {
        // 'recipes' variable should always be reset when returning to home, so if it is null a new
        // search has been performed
        if (viewModel.recipes.value == null) {
            viewModel.fetchRecipes(
                query = query,
                getApiResult = true,
                getDbResult = true
            )
        }
    }

    val queryState by viewModel.queryState.observeAsState()

    when (queryState) {
        SharedViewModel.QueryState.LOADING -> {
            LoadingScreen()
        }
        SharedViewModel.QueryState.SUCCESS -> {
            RecipeList(
                query = query,
                viewModel = viewModel,
                navController = navController,
                useSavedRecipes = false
            )
        }
        else -> {
            Text(text = "There was an error with retrieving the recipe data. Please try again in 10 seconds.")
        }
    }
}

@Composable
fun RecipeList(
    query: String = "",
    viewModel: SharedViewModel,
    navController: NavController,
    useSavedRecipes: Boolean
) {
    val recipes = when (useSavedRecipes) {
        true -> viewModel.savedRecipes.value!!.toList()
        false -> viewModel.recipes.value!!.results
    }
    val recipesPerPage = remember {
        viewModel.getSettingByKey("results_per_page").settingValue.toInt()
    }

    if (recipes.size > 0) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                RecipeResultCount(totalRecipesFound = viewModel.recipes.value!!.count)
            }
            itemsIndexed(recipes) { _, recipe ->
                RecipesListItem(
                    viewModel = viewModel,
                    navController = navController,
                    recipe = recipe,
                )
            }
            item {
                if (!useSavedRecipes) {
                    RecipePageSelector(
                        currentPage = viewModel.currentPage.value!!,
                        totalRecipesFound = viewModel.recipes.value!!.count,
                        recipesPerPage = recipesPerPage,
                        navigateToPage = { pageNumber ->
                            viewModel.fetchRecipes(
                                query = query,
                                pageNumber = pageNumber,
                                getApiResult = true,
                                getDbResult = true
                            )
                        }
                    )
                } else {
                    //RecipePageSelector for SavedRecipesScreen
                }
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "No recipes found",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun RecipeResultCount(totalRecipesFound: Int) {
    Text(
        text = "$totalRecipesFound results found",
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun RecipePageSelector(
    currentPage: Int,
    totalRecipesFound: Int,
    recipesPerPage: Int,
    navigateToPage: (Int) -> Unit
) {
    val pageRatio = totalRecipesFound / recipesPerPage
    val pageCount = if (totalRecipesFound % recipesPerPage == 0) {
        pageRatio
    } else {
        pageRatio + 1
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp),
        contentPadding = PaddingValues(all = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pageCount) { index ->
            Button(
                onClick = { navigateToPage(index) },
                modifier = Modifier.size(36.dp),
                enabled = currentPage != index
            ) {
                Text(
                    text = "${index + 1}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun RecipesListItem(
    viewModel: SharedViewModel,
    navController: NavController,
    recipe: Recipe
) {
    var imagePainter by remember { mutableStateOf<AsyncImagePainter?>(null) }

    val navigateToRecipeScreen: () -> Unit = {
        viewModel.setSelectedRecipeImagePainter(imagePainter!!)
        viewModel.setSelectedRecipe(recipe)
        navController.navigate(Route.RecipeScreenRoute.route) {
            launchSingleTop = true
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToRecipeScreen() }
    ) {
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(all = 8.dp)
                .fillMaxSize()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(164.dp)
                    .aspectRatio(1f)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(recipe.thumbnailUrl)
                        .build(),
                    contentDescription = "Recipe image",
                    contentScale = ContentScale.Crop
                ) {
                    imagePainter = painter

                    when (imagePainter!!.state) {
                        is AsyncImagePainter.State.Success ->
                            SubcomposeAsyncImageContent(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .fillMaxSize()
                            )
                        is AsyncImagePainter.State.Loading ->
                            RecipeImageLoadingIndicator()
                        else ->
                            Text(
                                text = "Error loading image",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
                    .height(164.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = recipe.name!!,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 24.sp,
                        fontWeight = FontWeight(600),
                        modifier = Modifier
                            .padding(vertical = 0.dp)
                            .fillMaxWidth(0.85f)
                    )
                    SaveRecipeIcon(
                        viewModel = viewModel,
                        recipe = recipe
                    )
                }
                recipe.description?.let {
                    Text(
                        text = it,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(300),
                        modifier = Modifier
                            .padding(all = 0.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun SaveRecipeIcon(
    viewModel: SharedViewModel,
    recipe: Recipe
) {
    var selected by remember { mutableStateOf(viewModel.isSavedRecipe(recipe)) }

    val iconModifier = Modifier
        .size(24.dp)

    IconButton(
        onClick = {
            if (selected) {
                viewModel.deleteSavedRecipe(recipe)
                selected = false
            } else {
                viewModel.saveRecipe(recipe)
                selected = true
            }
        },
        modifier = iconModifier
    ) {
        Crossfade(
            targetState = selected,
            animationSpec = tween(durationMillis = 500)
        ) { iconSelected ->
            if (iconSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favourite_filled_24),
                    contentDescription = "Delete saved recipe icon",
//                    modifier = iconModifier,
                    tint = Color.Red
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favourite_outline_24),
                    contentDescription = "Save recipe icon",
//                    modifier = iconModifier,
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun RecipeImageLoadingIndicator() {
    CircularProgressIndicator(
        color = Color.Gray,
        strokeWidth = 2.dp,
        modifier = Modifier
            .fillMaxSize(0.3f)
            .padding(4.dp)
    )
}
