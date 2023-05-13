package com.example.recipesearch.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.example.recipesearch.model.Components
import com.example.recipesearch.model.Instruction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.sql.Date

class Converters {

    @TypeConverter
    fun fromListToString(value: List<Any>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromStringToList(value: String?): List<Any>? {
        val listType = object : TypeToken<List<Any>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromInstructionListToString(value: List<Instruction>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromStringToInstructionList(value: String?): List<Instruction>? {
        val listType = object : TypeToken<List<Instruction>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromComponentsListToString(value: List<Components>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromStringToComponentsList(value: String?): List<Components>? {
        val listType = object : TypeToken<List<Components>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromAnyToString(value: Any?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromStringToAny(value: String?): Any? {
        val type = object : TypeToken<Any?>() {}.type
        return Gson().fromJson(value, type)
    }
}