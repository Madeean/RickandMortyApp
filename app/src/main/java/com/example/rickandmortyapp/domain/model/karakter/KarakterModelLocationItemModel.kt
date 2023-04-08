package com.example.rickandmortyapp.domain.model.karakter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KarakterModelLocationItemModel(
    val name:String?,
    val url:String?
):Parcelable
