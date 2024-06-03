package com.madeean.domain.tmdb.model


data class TmdbTrailerResponseDomainModel(
    val id:Int,
    val results:List<TmdbTrailerDomainModel>
)
