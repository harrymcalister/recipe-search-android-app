package com.example.recipesearch.repositories

import android.content.Context
import com.example.recipesearch.database.RecipeSearchDatabase
import com.example.recipesearch.database.savedrecipe.SavedRecipe
import com.example.recipesearch.model.Recipe
import com.example.recipesearch.model.RecipeResult
import com.example.recipesearch.model.toSavedRecipe
import com.example.recipesearch.network.RecipeApi

object MainRepositoryImpl: MainRepository {

    private val api = RecipeApi
    // Database is initialised in SharedViewModel to pass the context given by the ViewModelFactory
    private lateinit var database: RecipeSearchDatabase

    fun initialise(context: Context) {
        database = RecipeSearchDatabase.getDatabase(context)
    }

    override suspend fun getRecipes(query: String): RecipeResult {
        return api.retrofitService.getRecipes(query = query)
    }

    // Convert all SavedRecipe to Recipe by removing id
    override suspend fun getAllSavedRecipes(): List<SavedRecipe> {
        return database.getSavedRecipeDao().getAllSavedRecipes()
    }

    override suspend fun insertSavedRecipe(recipe: Recipe) {
        database.getSavedRecipeDao().insertSavedRecipe(recipe.toSavedRecipe())
    }

    override suspend fun deleteSavedRecipe(savedRecipe: SavedRecipe) {
        TODO("Not yet implemented")
    }
}