package com.example.rickandmortyapp.domain.tmdb.model


data class TmdbTrailerResponseDomainModel(
    val id:Int,
    val results:List<TmdbTrailerDomainModel>
)
