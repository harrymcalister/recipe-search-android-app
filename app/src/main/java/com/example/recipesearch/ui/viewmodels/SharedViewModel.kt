package com.example.recipesearch.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImagePainter
import com.example.recipesearch.database.setting.Setting
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

    private var _selectedRecipe = MutableLiveData<Recipe?>(null)
    val selectedRecipe: LiveData<Recipe?> = _selectedRecipe

    private var _recipes = MutableLiveData<RecipeResult?>(null)
    val recipes: LiveData<RecipeResult?> = _recipes

    private var _currentPage = MutableLiveData<Int>(0)
    val currentPage: LiveData<Int> = _currentPage

    private var _queryState = MutableLiveData(QueryState.LOADING)
    val queryState: LiveData<QueryState> = _queryState

    private var _savedRecipes = MutableLiveData<MutableList<Recipe>>(mutableListOf())
    val savedRecipes: LiveData<MutableList<Recipe>> = _savedRecipes

    private var _settings = MutableLiveData<MutableList<Setting>>(mutableListOf())
    val settings: LiveData<MutableList<Setting>> = _settings

//    private val DEFAULT_SETTINGS = listOf(
//        Setting(
//            id = 0,
//            settingKey = "results_per_page",
//            settingValue = "10",
//        ),
//        Setting(
//            id = 1,
//            settingKey = "max_results",
//            settingValue = "50",
//        ),
//        Setting(
//            id = 2,
//            settingKey = "units",
//            settingValue = "Metric",
//        )
//    )

    enum class QueryState {
        LOADING,
        SUCCESS,
        ERROR
    }

    fun initialiseSavedRecipes() {
        if (savedRecipes.value!!.size == 0) {
            fetchRecipes(
                getApiResult = false,
                getDbResult = true
            )
        }
    }

    fun initialiseSettings() {
        if (_settings.value!!.size == 0) {
            fetchSettings()
        }
    }

    private fun fetchSettings() {
        _queryState.value = QueryState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            var dbResult: List<Setting> = listOf()
            var resolvedQueryState: QueryState
            try {
                // Fetched saved recipes from local database
                dbResult = repository.getAllSettings()
                resolvedQueryState = QueryState.SUCCESS
            } catch (e: Exception) {
                resolvedQueryState = QueryState.ERROR
                Log.e("SharedViewModel.kt", "Query failed: $e")
            }
            withContext(Dispatchers.Main) {
                _settings.value = dbResult.toMutableList()
                _queryState.value = resolvedQueryState
            }
        }
    }

    fun getSettingByKey(settingKey: String): Setting {
        val settingIndex = _settings.value!!.indexOfFirst { setting ->
            setting.settingKey == settingKey
        }

        return _settings.value!![settingIndex]
    }

    fun updateSetting(settingKey: String, newSettingValue: String): Boolean {
        var updateWasSuccessful = false
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateSetting(
                    settingKey = settingKey,
                    newSettingValue = newSettingValue
                )

                getSettingByKey(settingKey).settingValue = newSettingValue

                withContext(Dispatchers.Main) {
                    updateWasSuccessful = true
                }
            } catch(e: Exception) {
                Log.e("SharedViewModel.kt", "Update failed: $e")
            }
        }
        return updateWasSuccessful
    }

    fun fetchRecipes(
        query: String = "",
        pageNumber: Int = 0,
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
                if (getApiResult) { apiResult = repository.getRecipes(
                    query = query,
                    pageNumber = pageNumber
                ) }
                // Fetched saved recipes from local database
                if (getDbResult) { dbResult = repository.getAllSavedRecipes() }
                resolvedQueryState = QueryState.SUCCESS
            } catch (e: Exception) {
                resolvedQueryState = QueryState.ERROR
                Log.e("SharedViewModel.kt", "Query failed: $e")
            }
            withContext(Dispatchers.Main) {
                if (getApiResult) { _recipes.value = apiResult }
                if (getDbResult) { _savedRecipes.value = dbResult.toMutableList() }
                _queryState.value = resolvedQueryState
            }
        }
    }

    fun isSavedRecipe(recipe: Recipe): Boolean {
        val id = recipe.recipeApiId
        return savedRecipes.value!!.any { savedRecipe -> savedRecipe.recipeApiId == id }
    }

    fun clearRecipes() {
        _recipes.value = null
        _currentPage.value = 0
        _queryState.value = QueryState.SUCCESS
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
                _savedRecipes.value!!.apply {
                    this.removeAt(this.indexOfFirst { savedRecipe ->
                        savedRecipe.recipeApiId == recipe.recipeApiId
                    })
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

    fun setSelectedRecipe(recipe: Recipe) {
        _selectedRecipe.value = recipe
    }

    fun setQueryState(queryState: QueryState) {
        _queryState.value = queryState
    }
}