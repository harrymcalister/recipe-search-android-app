package com.example.recipesearch.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesearch.model.RecipeResult
import com.example.recipesearch.repositories.MainRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(
    private val repository: MainRepositoryImpl
) : ViewModel() {

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
        viewModelScope.launch(Dispatchers.IO) {
            _queryState.value = QueryState.LOADING
            try {
                _recipes.value = repository.getRecipes(query)
                _queryState.value = QueryState.SUCCESS
            } catch (e: Exception) {
                _queryState.value = QueryState.ERROR
            }
        }
    }
}