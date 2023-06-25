package com.example.recipesearch.database

import androidx.room.TypeConverter
import com.example.recipesearch.model.Instruction
import com.example.recipesearch.model.Section
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
    fun fromSectionListToString(value: List<Section>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromStringToSectionList(value: String?): List<Section>? {
        val listType = object : TypeToken<List<Section>?>() {}.type
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