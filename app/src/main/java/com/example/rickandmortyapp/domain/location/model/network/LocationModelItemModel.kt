package com.example.rickandmortyapp.domain.location.model.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModelItemModel(
    val id: Int?,
    val name: String?,
    val type: String?,
    val dimension: String?,
    val residents: List<String>?,
    val created: String?
):Parcelable
