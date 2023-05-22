package com.example.recipesearch.ui.screens

import android.content.res.Resources
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.example.recipesearch.model.Recipe
import com.example.recipesearch.repositories.MainRepositoryImpl
import com.example.recipesearch.ui.viewmodels.SharedViewModel
import com.example.recipesearch.ui.viewmodels.SharedViewModelFactory
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.*
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.request.ImageRequest
import com.example.recipesearch.R
import com.example.recipesearch.model.RecipeResult
import com.example.recipesearch.navigation.Route

@Composable
fun SearchScreen(
    viewModel: SharedViewModel,
    navController: NavController,
    query: String
) {
    LaunchedEffect(Unit) {
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
            RecipesList(
                viewModel = viewModel,
                navController = navController
            )
        }
        else -> {
            Text(text = "There was an error with retrieving the recipe data. Please try again in 10 seconds.")
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = Color.Gray,
            strokeWidth = 4.dp,
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
        )
    }
}

@Composable
fun RecipesList(
    viewModel: SharedViewModel,
    navController: NavController
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(viewModel.recipes.value!!.results) { index, recipe ->
            RecipesListItem(
                viewModel = viewModel,
                navController = navController,
                recipesListIndex = index,
                recipe = recipe
            )
        }
    }
}

@Composable
fun RecipesListItem(
    viewModel: SharedViewModel,
    navController: NavController,
    recipesListIndex: Int,
    recipe: Recipe
) {
    var imagePainter by remember { mutableStateOf<AsyncImagePainter?>(null) }

    val navigateToRecipeScreen: (Int) -> Unit = { recipesIndex ->
        viewModel.setSelectedRecipeImagePainter(imagePainter!!)
        navController.navigate(Route.RecipeScreenRoute.withArgs(recipesIndex.toString())) {
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
            .clickable { navigateToRecipeScreen(recipesListIndex) }
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
                Text(
                    text = recipe.description!!,
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
            Log.e("SearchScreen.kt", "Favourite icon clicked")
        },
        modifier = iconModifier
    ) {
        Crossfade(
            targetState = selected,
            animationSpec = tween(durationMillis = 300)
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
