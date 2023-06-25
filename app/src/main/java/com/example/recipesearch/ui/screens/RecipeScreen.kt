package com.example.recipesearch.ui.screens

import android.util.Log
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

        selectedRecipe.sections?.get(0)?.let {
            RecipeIngredients(
                ingredients = it.ingredients,
                measurementSystem = "metric"
            )
        }

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

// Designed for 2 or fewer measurement unit options to be given, if more are given the recipe
// will not be shown in the search results
fun getCorrectMeasurementAndPlurality(
    ingredient: Ingredient,
    measurementSystem: String
): String {
//    val ingredientMeasurements = ingredient.measurements
    var chosenMeasurement: Measurement?
    chosenMeasurement = if (ingredient.measurements.size == 1) {
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
    if (chosenMeasurement == null)
    if (chosenMeasurement.unit.system == "none") {}
    val (unitIsPlural, nameIsPlural) = getCorrectPlurality(chosenMeasurement)
    val unitString =
    val nameString = if (chosenMeasurement == null) {
        ingredient.ingredientName.displaySingular.replaceFirstChar { char ->
            char.uppercase()
        }
    } else if (chosenMeasurement.quantity == "1") {
        ingredient.ingredientName.displaySingular
    } else {
        ingredient.ingredientName.displayPlural
    }
    // check if only one measurement exists, if so use it

    // if >1 measurements exist, take the one that matches measurementSystem
    // unless there are no matches, then take the first one

    // else there are no measurements, just write ingredient name

    // if chosen measurement null, just write ingredient name

    // quantity + measurement unit abbreviation is measurement string

    if (ingredient.measurements.size == 1) {
        // add plurality check
        return "${ingredient.measurements[0].quantity}${ingredient.measurements[0].unit.abbreviation} ${ingredient.ingredientName.displaySingular}"
    } else {
        val correctMeasurement: Measurement? = try {
            val correctMeasurement = ingredient.measurements.first { measurement ->
                measurement.unit.system == measurementSystem
            }
            correctMeasurement
        } catch (e: Exception) {
            Log.e("RecipeScreen.kt", "METRIC OR IMPERIAL MEASUREMENTS NOT FOUND: $e")
            null
        }

        return if (correctMeasurement == null) {
            ingredient.ingredientName.displaySingular
        } else if (correctMeasurement.unit.system == "none") {
            when (correctMeasurement.quantity) {
                "1" ->  "${correctMeasurement.quantity}${correctMeasurement.unit.abbreviation} ${ingredient.ingredientName.displaySingular}"
                else -> "${correctMeasurement.quantity}${correctMeasurement.unit.abbreviation} ${ingredient.ingredientName.displayPlural}"
            }
        } else {
            "${correctMeasurement.quantity}${correctMeasurement.unit.abbreviation} ${ingredient.ingredientName.displaySingular}"
        }
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
            "none"  -> Pair(false, true)
            else    -> Pair(false, false)
        }
    } else {
        return when (measurement.unit.system) {
            "none"  -> Pair(true, false)
            else    -> Pair(false, false)
        }
    }
//      //take below code into account into determining plurality
//    if (chosenMeasurement == null) {
//        ""
//    } else if (chosenMeasurement.unit.displaySingular
//        == chosenMeasurement.unit.displaySingular) {
//        "${chosenMeasurement.quantity}${chosenMeasurement.unit.displaySingular} "
//    } else {
//        if (chosenMeasurement.quantity == "1") {
//            "${chosenMeasurement.quantity} ${chosenMeasurement.unit.displaySingular} "
//        } else {
//            "${chosenMeasurement.quantity} ${chosenMeasurement.unit.displayPlural} "
//        }
//    }
}

@Composable
fun RecipeIngredients(ingredients: List<Ingredient>, measurementSystem: String) {
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

    ingredients.forEach { ingredient ->
        Text(
            text = "• ${ getCorrectMeasurementAndPlurality(
                ingredient = ingredient,
                measurementSystem = measurementSystem
            ) }" +
                    "${ingredient.ingredientName}" +
                    "${ingredient.measurements}",
            color = Color.DarkGray,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight(300),
            textAlign = TextAlign.Start,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            maxLines = Int.MAX_VALUE,
            modifier = Modifier.fillMaxWidth()
        )
//        if (ingredient.measurements.size == 1) {
//            Text(
//                text = "• ${getCorrectMeasurementAndPlurality()}",
//                color = Color.DarkGray,
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight(300),
//                textAlign = TextAlign.Start,
//                fontSize = 24.sp,
//                lineHeight = 32.sp,
//                maxLines = Int.MAX_VALUE,
//                modifier = Modifier.fillMaxWidth()
//            )
//        } else if (ingredient.measurements.size > 1) {
//            val correctMeasurement = getCorrectMeasurement(
//                ingredient = ingredient,
//                measurementSystem = measurementSystem
//            )
//            Text(
//                text = "• ${correctMeasurement} ${}",
//                color = Color.DarkGray,
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight(300),
//                textAlign = TextAlign.Start,
//                fontSize = 24.sp,
//                lineHeight = 32.sp,
//                maxLines = Int.MAX_VALUE,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//        ingredient.measurements.forEach { measurement ->
//            Text(
//                text = "• ${measurement.quantity} ${measurement.unit}",
//                color = Color.DarkGray,
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight(300),
//                textAlign = TextAlign.Start,
//                fontSize = 24.sp,
//                lineHeight = 32.sp,
//                maxLines = Int.MAX_VALUE,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//        Text(
//            text = "${ingredient.ingredientName}\n",
//            color = Color.DarkGray,
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight(300),
//            textAlign = TextAlign.Start,
//            fontSize = 24.sp,
//            lineHeight = 32.sp,
//            maxLines = Int.MAX_VALUE,
//            modifier = Modifier.fillMaxWidth()
//        )
    }
}