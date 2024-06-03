package com.madeean.domain.karakter.model.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KarakterModelOriginItemModel (
    val name:String?,
    val url:String?
    ):Parcelable