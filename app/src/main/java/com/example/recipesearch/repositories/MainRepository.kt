package com.example.recipesearch.repositories

import com.example.recipesearch.database.savedrecipe.SavedRecipe
import com.example.recipesearch.model.Recipe
import com.example.recipesearch.model.RecipeResult

interface MainRepository {

    suspend fun getRecipes(query: String): RecipeResult

    suspend fun getAllSavedRecipes(): List<SavedRecipe>

    suspend fun insertSavedRecipe(recipe: Recipe)

    suspend fun deleteSavedRecipe(recipe: SavedRecipe)
}