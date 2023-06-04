package com.example.recipesearch.repositories

import android.content.Context
import com.example.recipesearch.database.RecipeSearchDatabase
import com.example.recipesearch.database.savedrecipe.toRecipe
import com.example.recipesearch.database.setting.Setting
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
        val retrievedRecipes = api.retrofitService.getRecipes(query = query)
        return filterCompatibleRecipes(recipeApiResult = retrievedRecipes)
    }

    private fun filterCompatibleRecipes(recipeApiResult: RecipeResult): RecipeResult {
        val compatibleRecipes = mutableListOf<Recipe>()
        for (recipe in recipeApiResult.results) {
            if (recipe.name == null ||
                recipe.thumbnailUrl == null ||
                recipe.sections == null ||
                recipe.instructions == null) {
                continue
            }
            compatibleRecipes.add(recipe)
        }
        return RecipeResult(
            count = compatibleRecipes.size,
            results = compatibleRecipes
        )
    }

    // Cast all SavedRecipe to Recipe by removing id
    override suspend fun getAllSavedRecipes(): List<Recipe> {
        val savedRecipes = database.getSavedRecipeDao().getAllSavedRecipes()
        val castRecipes = mutableListOf<Recipe>()
        for (savedRecipe in savedRecipes) {
            castRecipes.add(savedRecipe.toRecipe())
        }
        return castRecipes
    }

    override suspend fun insertSavedRecipe(recipe: Recipe) {
        database.getSavedRecipeDao().insertSavedRecipe(recipe.toSavedRecipe())
    }

    override suspend fun deleteSavedRecipe(recipe: Recipe) {
        database.getSavedRecipeDao().deleteSavedRecipe(recipeApiId = recipe.recipeApiId)
    }

    override suspend fun getAllSettings(): List<Setting> {
        return database.getSettingDao().getAllSettings()
    }

    override suspend fun updateSetting(settingKey: String, newSettingValue: String) {
        database.getSettingDao().updateSetting(
            key = settingKey,
            newValue = newSettingValue
        )
    }
}