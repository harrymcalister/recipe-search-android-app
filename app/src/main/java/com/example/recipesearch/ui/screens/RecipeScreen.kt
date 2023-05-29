package com.example.recipesearch.ui.screens

import android.icu.util.Measure
import android.util.EventLogTags.Description
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DividerDefaults.color
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImageContent
import com.example.recipesearch.ui.viewmodels.SharedViewModel
import com.example.recipesearch.R
import com.example.recipesearch.model.*

@Composable
fun RecipeScreen(
    viewModel: SharedViewModel,
    navController: NavController
) {
    val selectedRecipe = viewModel.selectedRecipe.value!!

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .padding(all = 16.dp)
    ) {
        RecipeTitle(title = selectedRecipe.name!!)
        
        RecipeImage(viewModel = viewModel)

        selectedRecipe.description?.let {
            RecipeDescription(description = it)
        }

        RecipeDivider()

        val recipeHasTimeData = selectedRecipe.totalTimeMinutes != null &&
                selectedRecipe.prepTimeMinutes != null &&
                selectedRecipe.cookTimeMinutes != null
        if (recipeHasTimeData) {
            RecipeTime(
                totalTimeMinutes = selectedRecipe.totalTimeMinutes!!,
                prepTimeMinutes = selectedRecipe.prepTimeMinutes!!,
                cookTimeMinutes = selectedRecipe.cookTimeMinutes!!
            )

            RecipeDivider()
        }

        RecipeIngredients(
            ingredients = selectedRecipe.sections[0].ingredients,
            measurementSystem = MeasurementSystem.METRIC
        )

        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleLarge
        )

        selectedRecipe.instructions!!.forEach { instruction ->
            Text(
                text = "${instruction.position}) ${instruction.displayText}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun RecipeDivider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.DarkGray)
    )
}

@Composable
fun RecipeTitle(title: String) {
    Text(
        text = title,
        color = Color.DarkGray,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight(600),
        textAlign = TextAlign.Center,
        fontSize = 48.sp,
        lineHeight = 64.sp,
        maxLines = Int.MAX_VALUE,
        modifier = Modifier.fillMaxWidth()
    )
}
@Composable
fun RecipeImage(viewModel: SharedViewModel) {
    val imagePainter by remember { mutableStateOf(viewModel.selectedRecipeImagePainter.value!!) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        when (imagePainter.state) {
            is AsyncImagePainter.State.Success ->
                Image(
                    painter = imagePainter,
                    contentDescription = "Recipe image",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
                )
            is AsyncImagePainter.State.Loading ->
                RecipeImageLoadingIndicator()
            else ->
                Text(
                    text = "Error loading image",
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
        }
    }
}

@Composable
fun RecipeDescription(description: String) {
    Text(
        text = description,
        color = Color.DarkGray,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight(300),
        textAlign = TextAlign.Start,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        maxLines = Int.MAX_VALUE,
        modifier = Modifier.fillMaxWidth()
    )
}

fun parseTimeInMinutes(timeMins: Int): String {
    val hours = timeMins / 60
    val mins = timeMins % 60
    val timeString = if (hours == 0) {
        "$mins mins"
    } else if (hours == 1) {
        "$hours hr $mins mins"
    } else {
        "$hours hrs $mins mins"
    }
    return timeString
}

@Composable
fun RecipeTime(
    totalTimeMinutes: Int,
    prepTimeMinutes: Int,
    cookTimeMinutes: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_timer_icon),
            contentDescription = "Timer icon",
            modifier = Modifier.height(24.dp)
        )

        val totalTime = parseTimeInMinutes(totalTimeMinutes)
        val prepTime = parseTimeInMinutes(prepTimeMinutes)
        val cookTime = parseTimeInMinutes(cookTimeMinutes)

        Column {
            Text(
                text = "Total time - $totalTime",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(300),
                textAlign = TextAlign.Start,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Prep time - $prepTime",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(300),
                textAlign = TextAlign.Start,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Cook time - $cookTime",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(300),
                textAlign = TextAlign.Start,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}