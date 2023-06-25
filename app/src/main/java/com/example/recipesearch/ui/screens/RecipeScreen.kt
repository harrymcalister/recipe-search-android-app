package com.example.recipesearch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
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
            sections = selectedRecipe.sections!!,
            measurementSystem = "metric"
        )

        RecipeInstructions(instructions = selectedRecipe.instructions!!)
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

fun getCorrectMeasurementAndPlurality(
    ingredient: Ingredient,
    measurementSystem: String
): String {
    val correctMeasurement: Measurement = getCorrectMeasurement(
        ingredient = ingredient,
        measurementSystem = measurementSystem
    ) ?: return ingredient.ingredientName.displayPlural.replaceFirstChar { char ->
            char.uppercase()
    }

    val (unitIsPlural, nameIsPlural) = getCorrectPlurality(correctMeasurement)
    val nameString = if (nameIsPlural) {
        ingredient.ingredientName.displayPlural
    } else {
        ingredient.ingredientName.displaySingular
    }
    val unitString = if (unitIsPlural) {
        correctMeasurement.unit.displayPlural
    } else {
        correctMeasurement.unit.displaySingular
    }
    val quantityString = correctMeasurement.quantity

    return "$quantityString $unitString $nameString"
}

fun getCorrectMeasurement(
    ingredient: Ingredient,
    measurementSystem: String
): Measurement? {
    return if (ingredient.measurements.size == 1) {
        ingredient.measurements[0]
    } else if (ingredient.measurements.size > 1) {
        try {
            ingredient.measurements.first { measurement ->
                measurement.unit.system == measurementSystem
            }
        } catch (e: NoSuchElementException) {
            ingredient.measurements[0]
        }
    } else {
        null
    }
}

/**
 * @params A nullable [Measurement] for which the plurality should be evaluated
 * @return A [Boolean] [Pair] representing <unitIsPlural, nameIsPlural>
 */
fun getCorrectPlurality(measurement: Measurement?): Pair<Boolean, Boolean> {
    if (measurement == null) {
        return Pair(false, false)
    } else if (measurement.quantity == "1") {
        return when (measurement.unit.system) {
            "none", "null", null -> Pair(false, false)
            else -> Pair(false, false)
        }
    } else {
        return when (measurement.unit.system) {
            "none", "null", null -> Pair(true, true)
            else -> Pair(true, false)
        }
    }
}

fun getIngredientString(
    ingredient: Ingredient,
    measurementSystem: String
): String {
    val extraComment = ingredient.extraComment ?: ""
    val extraCommentString = if (extraComment.isNotBlank()) ", $extraComment" else ""
    val ingredientString = ingredient.rawText ?:
        // Backup string in case ingredient data doesn't provide 'raw_text'
        (getCorrectMeasurementAndPlurality(
            ingredient = ingredient,
            measurementSystem = measurementSystem
        ) + extraCommentString)

    return "• $ingredientString"
}

@Composable
fun RecipeIngredients(
    sections: List<Section>,
    measurementSystem: String
) {
    Text(
        text = "Ingredients",
        color = Color.DarkGray,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight(600),
        textAlign = TextAlign.Start,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth()
    )

    sections.forEach { section ->

        section.name?.let { name ->
            Text(
                text = name,
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(300),
                textAlign = TextAlign.Start,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }

        section.ingredients.forEach { ingredient ->
            Text(
                text = getIngredientString(
                    ingredient = ingredient,
                    measurementSystem = measurementSystem
                ),
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
    }
}

@Composable
fun RecipeInstructions(instructions: List<Instruction>) {
    Text(
        text = "Instructions",
        color = Color.DarkGray,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight(600),
        textAlign = TextAlign.Start,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth()
    )

    instructions.forEach { instruction ->
        Text(
            text = "${instruction.position}) ${instruction.displayText}",
            style = MaterialTheme.typography.titleMedium
        )
    }
}