package com.example.recipesearch.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImagePainter
import com.example.recipesearch.database.savedrecipe.SavedRecipe
import com.example.recipesearch.model.Recipe
import com.example.recipesearch.model.RecipeResult
import com.example.recipesearch.model.toSavedRecipe
import com.example.recipesearch.repositories.MainRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel(
    private val repository: MainRepositoryImpl,
    context: Context
) : ViewModel() {

    init {
        MainRepositoryImpl.initialise(context = context)
    }

    private var _selectedRecipeImagePainter = MutableLiveData<AsyncImagePainter?>(null)
    val selectedRecipeImagePainter: LiveData<AsyncImagePainter?> = _selectedRecipeImagePainter

    private var _recipes = MutableLiveData<RecipeResult?>(null)
    val recipes: LiveData<RecipeResult?> = _recipes

    private var _queryState = MutableLiveData(QueryState.LOADING)
    val queryState: LiveData<QueryState> = _queryState

    private var _savedRecipes = MutableLiveData<MutableList<Recipe>>(mutableListOf())
    val savedRecipes: LiveData<MutableList<Recipe>> = _savedRecipes

    enum class QueryState {
        LOADING,
        SUCCESS,
        ERROR
    }

    fun fetchRecipes(
        query: String = "",
        getApiResult: Boolean,
        getDbResult: Boolean
    ) {
        _queryState.value = QueryState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            var apiResult: RecipeResult? = null
            var dbResult: List<Recipe> = listOf()
            var resolvedQueryState: QueryState
            try {
                // Fetch searched recipes from API
                if (getApiResult) { apiResult = repository.getRecipes(query) }
                // Fetched saved recipes from local database
                if (getDbResult) { dbResult = repository.getAllSavedRecipes() }
                resolvedQueryState = QueryState.SUCCESS
                Log.d("SharedViewModel.kt", "Query retrieved successfully")
            } catch (e: Exception) {
                resolvedQueryState = QueryState.ERROR
                Log.e("SharedViewModel.kt", "Query failed: $e")
            }
            withContext(Dispatchers.Main) {
                if (getApiResult) { _recipes.value = apiResult }
                if (getDbResult) { _savedRecipes.value = dbResult as MutableList<Recipe>? }
                _queryState.value = resolvedQueryState
                Log.d("SharedViewModel.kt", "Query state updated to ${_queryState.value}")
            }
        }
    }

    fun isSavedRecipe(recipe: Recipe): Boolean {
        val id = recipe.recipeApiId
        return savedRecipes.value!!.any { savedRecipe -> savedRecipe.recipeApiId == id }
    }

    fun clearRecipes() {
        _recipes.value = null
        _queryState.value = QueryState.LOADING
    }

    fun saveRecipe(recipe: Recipe): Boolean {
        var insertWasSuccessful = false
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertSavedRecipe(recipe)
                _savedRecipes.value!!.add(recipe)
                withContext(Dispatchers.Main) {
                    insertWasSuccessful = true
                }
            } catch(e: Exception) {
                Log.e("SharedViewModel.kt", "Insert failed: $e")
            }
        }
        return insertWasSuccessful
    }

    fun deleteSavedRecipe(recipe: Recipe): Boolean {
        var deleteWasSuccessful = false
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteSavedRecipe(recipe)
                _savedRecipes.value!!.first { savedRecipe ->
                    savedRecipe.recipeApiId == recipe.recipeApiId
                }
                withContext(Dispatchers.Main) {
                    deleteWasSuccessful = true
                }
            } catch(e: Exception) {
                Log.e("SharedViewModel.kt", "Delete failed: $e")
            }
        }
        return deleteWasSuccessful
    }

    fun setSelectedRecipeImagePainter(painter: AsyncImagePainter) {
        _selectedRecipeImagePainter.value = painter
    }
}