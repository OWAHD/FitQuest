package com.example.fitquest.data.room

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return json.decodeFromString(data)
    }
}