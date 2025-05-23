package com.example.fitsphere.data.local.database.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatLngEntity(
    val latitude: Double,
    val longitude: Double
) : Parcelable