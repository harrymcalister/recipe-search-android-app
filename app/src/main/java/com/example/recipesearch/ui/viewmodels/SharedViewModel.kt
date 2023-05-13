package com.example.recipesearch.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImagePainter
import com.example.recipesearch.model.Recipe
import com.example.recipesearch.model.RecipeResult
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

    enum class QueryState {
        LOADING,
        SUCCESS,
        ERROR
    }

    fun fetchRecipes(query: String) {
        _queryState.value = QueryState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            var queryResult: RecipeResult? = null
            var resolvedQueryState: QueryState
            try {
                queryResult = repository.getRecipes(query)
                resolvedQueryState = QueryState.SUCCESS
                Log.d("SharedViewModel.kt", "Query retrieved successfully")
                val databaseSizeBefore = repository.getAllSavedRecipes().size
                Log.d("SharedViewModel.kt", "Saved recipes size before: $databaseSizeBefore")
                repository.insertSavedRecipe(queryResult.results[1])
                val databaseSizeAfter = repository.getAllSavedRecipes().size
                Log.d("SharedViewModel.kt", "Saved recipes size after: $databaseSizeAfter")
            } catch (e: Exception) {
                resolvedQueryState = QueryState.ERROR
                Log.e("SharedViewModel.kt", "Query failed: $e")
            }
            withContext(Dispatchers.Main) {
                _recipes.value = queryResult
                _queryState.value = resolvedQueryState
                Log.d("SharedViewModel.kt", "Query state updated to ${_queryState.value}")
            }
        }
    }

    fun clearRecipes() {
        _recipes.value = null
        _queryState.value = QueryState.LOADING
    }

    fun setSelectedRecipeImagePainter(painter: AsyncImagePainter) {
        _selectedRecipeImagePainter.value = painter
    }
}