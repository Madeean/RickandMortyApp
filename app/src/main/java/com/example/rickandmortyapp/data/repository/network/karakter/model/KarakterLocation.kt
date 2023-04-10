package com.example.rickandmortyapp.data.repository.network.karakter.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KarakterLocation(
    val name:String?,
    val url:String?
):Parcelable
