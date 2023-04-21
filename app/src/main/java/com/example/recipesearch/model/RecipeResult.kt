package com.example.recipesearch.model

data class RecipeResult(
    val count: Int,
    val results: List<Recipe>,
)