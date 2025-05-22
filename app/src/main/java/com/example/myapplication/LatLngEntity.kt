package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatLngEntity(
    val latitude: Double,
    val longitude: Double
) : Parcelable
