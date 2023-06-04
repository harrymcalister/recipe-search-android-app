package com.example.recipesearch.repositories

import com.example.recipesearch.database.savedrecipe.SavedRecipe
import com.example.recipesearch.database.setting.Setting
import com.example.recipesearch.model.Recipe
import com.example.recipesearch.model.RecipeResult

interface MainRepository {

    suspend fun getRecipes(query: String): RecipeResult

    suspend fun getAllSavedRecipes(): List<Recipe>

    suspend fun insertSavedRecipe(recipe: Recipe)

    suspend fun deleteSavedRecipe(recipe: Recipe)

    suspend fun getAllSettings(): List<Setting>

    suspend fun updateSetting(settingKey: String, newSettingValue: String)
}