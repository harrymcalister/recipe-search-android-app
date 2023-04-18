package com.example.recipesearch.repositories

import com.example.recipesearch.model.RecipeResult
import com.example.recipesearch.network.RecipeApi

object MainRepositoryImpl: MainRepository {

    private val api = RecipeApi

    override suspend fun getRecipes(query: String): RecipeResult {
        return api.retrofitService.getRecipes(query = query)
    }
}