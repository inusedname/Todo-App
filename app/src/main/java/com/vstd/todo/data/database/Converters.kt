package com.vstd.todo.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vstd.todo.data.Subtask

class Converters {
    @TypeConverter
    fun fromJson(jsonString: String): List<Subtask> {
        val listType = object : TypeToken<List<Subtask>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    @TypeConverter
    fun fromStringList(list: List<Subtask>): String = Gson().toJson(list)
}