package com.example.recipesearch.database.savedrecipe

import androidx.room.*
import com.example.recipesearch.model.Recipe

@Dao
interface SavedRecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllSavedRecipes(): List<SavedRecipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSavedRecipe(savedRecipe: SavedRecipe)

    @Delete
    fun deleteSavedRecipe(savedRecipe: SavedRecipe)

}