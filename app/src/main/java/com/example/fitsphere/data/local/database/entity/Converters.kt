package com.example.myapplication.data.local.database.entity

import androidx.room.TypeConverter
import com.example.myapplication.data.local.database.entity.LatLngEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromLatLngList(value: ArrayList<LatLngEntity>): String {
        return Gson().toJson(value)
    }


    @TypeConverter
    fun toLatLngList(value: String): ArrayList<LatLngEntity> {
        val listType = object : TypeToken<List<LatLngEntity>>() {}.type
        return Gson().fromJson<List<LatLngEntity>>(value, listType) as ArrayList<LatLngEntity>
    }

}