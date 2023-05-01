package com.example.recipesearch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImageContent
import com.example.recipesearch.ui.viewmodels.SharedViewModel

@Composable
fun RecipeScreen(
    viewModel: SharedViewModel,
    navController: NavController,
    recipesIndex: Int
) {
    val selectedRecipe = viewModel.recipes.value!!.results[recipesIndex]

    Column {
        Text(
            text = "Selected recipe: ${selectedRecipe.name}",
            style = MaterialTheme.typography.titleLarge
        )
        
        RecipeImage(viewModel = viewModel)

        Text(
            text = "Description: ${selectedRecipe.description}",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Total time: ${selectedRecipe.totalTimeMinutes}",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Ingredients:",
            style = MaterialTheme.typography.titleLarge
        )
        selectedRecipe.sections[0].ingredients.forEach { ingredient ->
            Text(
                text = "- ${ingredient.ingredientName}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            text = "Instructions:",
            style = MaterialTheme.typography.titleLarge
        )

        selectedRecipe.instructions.forEach { instruction ->
            Text(
                text = "${instruction.position}) ${instruction.displayText}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun RecipeImage(viewModel: SharedViewModel) {
    val imagePainter = viewModel.selectedRecipeImagePainter.value!!
    when (imagePainter.state) {
        is AsyncImagePainter.State.Success ->
            Image(
                painter = imagePainter, contentDescription = "Recipe image"
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