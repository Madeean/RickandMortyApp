package com.example.rickandmortyapp.domain.model.episode

import com.google.gson.annotations.SerializedName

data class EpisodeModelItemModel(
    val id:Int?,
    val name:String?,
    val airDate:String?,
    val episode:String?,
    val created:String?,
    val characterList:List<String>?,
)
