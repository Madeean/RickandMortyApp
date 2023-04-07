package com.example.rickandmortyapp.domain.model.episode

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
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
