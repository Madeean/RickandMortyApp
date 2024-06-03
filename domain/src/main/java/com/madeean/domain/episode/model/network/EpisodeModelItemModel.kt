package com.madeean.domain.episode.model.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeModelItemModel(
    val id:Int?,
    val name:String?,
    val airDate:String?,
    val episode:String?,
    val created:String?,
    val characterList:List<String>?,
): Parcelable
