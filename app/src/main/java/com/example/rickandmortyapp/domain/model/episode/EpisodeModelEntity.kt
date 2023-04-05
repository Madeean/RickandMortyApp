package com.example.rickandmortyapp.domain.model.episode

data class EpisodeModelEntity (
    val state:Int,
    val results: EpisodeModelItemModel?,
    val error:String?
)