package com.vstd.todo.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromJson(jsonString: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String = Gson().toJson(list)
}