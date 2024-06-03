package com.madeean.domain.karakter.model.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KarakterModelLocationItemModel(
    val name:String?,
    val url:String?
):Parcelable
