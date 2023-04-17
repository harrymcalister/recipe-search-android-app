package com.example.recipesearch.repositories

import com.example.recipesearch.model.Recipe
import com.example.recipesearch.model.RecipeResult

interface MainRepository {
    suspend fun getRecipes(query: String): RecipeResult
}