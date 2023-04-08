package com.example.rickandmortyapp.domain.model.karakter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KarakterModelOriginItemModel (
    val name:String?,
    val url:String?
    ):Parcelable