package com.example.recipesearch.database.setting

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SettingDao {

    @Query("SELECT * FROM settings")
    fun getAllSettings(): List<Setting>

    @Query("UPDATE settings SET setting_value = :newValue WHERE setting_key = :key")
    fun updateSetting(key: String, newValue: String)
}