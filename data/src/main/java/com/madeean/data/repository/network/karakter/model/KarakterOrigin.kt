package com.madeean.data.repository.network.karakter.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KarakterOrigin (
    val name:String?,
    val url:String?
): Parcelable