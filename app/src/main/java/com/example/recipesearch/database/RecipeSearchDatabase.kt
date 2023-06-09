package com.example.recipesearch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipesearch.database.savedrecipe.SavedRecipe
import com.example.recipesearch.database.savedrecipe.SavedRecipeDao
import com.example.recipesearch.database.setting.Setting
import com.example.recipesearch.database.setting.SettingDao

@Database(
    entities = [
        SavedRecipe::class,
        Setting::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RecipeSearchDatabase : RoomDatabase() {

    abstract fun getSavedRecipeDao(): SavedRecipeDao
    abstract fun getSettingDao(): SettingDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeSearchDatabase? = null

        fun getDatabase(context: Context): RecipeSearchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context,
                    klass = RecipeSearchDatabase::class.java,
                    name = "recipe_search")
                    .createFromAsset("database/recipe_search.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}

