package com.example.myapplication

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromLatLngList(value: List<LatLngEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toLatLngList(value: String): List<LatLngEntity> {
        val listType = object : TypeToken<List<LatLngEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
