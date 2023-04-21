package com.example.recipesearch.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.example.recipesearch.model.Recipe
import com.example.recipesearch.repositories.MainRepositoryImpl
import com.example.recipesearch.ui.viewmodels.SharedViewModel
import com.example.recipesearch.ui.viewmodels.SharedViewModelFactory
import androidx.compose.ui.platform.LocalDensity
import com.example.recipesearch.model.RecipeResult

@Composable
fun SearchScreen(
    navController: NavController,
    query: String
) {
    val viewModel: SharedViewModel = viewModel(
        factory = SharedViewModelFactory(MainRepositoryImpl)
    )
    val queryState by viewModel.queryState.observeAsState()

    LaunchedEffect(Unit) {
        Log.e("SearchScreen.kt", "launchedEffect initiated")
        viewModel.fetchRecipes(query)
    }

    when (queryState) {
        SharedViewModel.QueryState.LOADING -> {
            LoadingScreen()
        }
        SharedViewModel.QueryState.SUCCESS -> {
            TestCards(viewModel.recipes.value)
        }
        else -> {
            Text(text = "There was an error with retrieving the recipe data")
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
fun TestCards(value: RecipeResult?) {
    val recipeInstance: Recipe = Recipe(
        renditions = emptyList(),
        totalTimeTier = Any(),
        seoTitle = "",
        videoId = null,
        instructions = emptyList(),
        draftStatus = "",
        thumbnailAltText = "",
        credits = emptyList(),
        promotion = "",
        facebookPosts = emptyList(),
        brand = null,
        show = Any(),
        isOneTop = false,
        totalTimeMinutes = 0,
        servingsNounPlural = "",
        isShoppable = false,
        price = Any(),
        showId = 0,
        buzzId = null,
        tipsAndRatingsEnabled = false,
        videoUrl = null,
        approvedAt = 0L,
        nutritionVisibility = "",
        servingsNounSingular = "",
        name = "Test name",
        createdAt = 0L,
        sections = emptyList(),
        compilations = emptyList(),
        beautyUrl = null,
        originalVideoUrl = null,
        country = "",
        keywords = "",
        seoPath = null,
        prepTimeMinutes = 0,
        cookTimeMinutes = 0,
        description = "This recipe description is a sample recipe description",
        inspiredByUrl = null,
        topics = emptyList(),
        videoAdContent = null,
        language = "",
        userRatings = Any(),
        brandId = null,
        tags = emptyList(),
        canonicalId = "",
        slug = "",
        nutrition = Any(),
        thumbnailUrl = "https://images.unsplash.com/photo-1628373383885-4be0bc0172fa?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1301&q=80",
        yields = "",
        id = 0,
        numServings = 0,
        aspectRatio = "",
        updatedAt = 0L
    )
    val recipesList: List<Recipe> = listOf(recipeInstance,recipeInstance,recipeInstance,recipeInstance,recipeInstance,recipeInstance)
    RecipesList(recipesList)
}

@Composable
fun RecipesList(recipes: List<Recipe>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(recipes) {
            RecipesListItem(it)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun RecipesListItem(recipe: Recipe) {
    val cornerSizeAsPx = LocalDensity.current.run{ 12.dp.toPx() }
    val coilPainter = rememberImagePainter(
        data = recipe.thumbnailUrl,
        builder = { transformations(RoundedCornersTransformation(cornerSizeAsPx)) }
    )
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .aspectRatio(1f)
                    .padding(8.dp)
            ) {
                Image(
                    painter = coilPainter,
                    contentDescription = "Recipe image",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.6f)
            ) {
                Text(
                    text = recipe.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(PaddingValues(top = 8.dp, start = 8.dp, end = 8.dp))
                )
                Text(
                    text = recipe.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(200),
                    modifier = Modifier.padding(PaddingValues(top = 8.dp, start = 8.dp, end = 8.dp))
                )
            }
        }
    }
}