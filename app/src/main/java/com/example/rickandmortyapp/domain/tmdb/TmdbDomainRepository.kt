package com.example.rickandmortyapp.domain.tmdb

import com.example.rickandmortyapp.domain.tmdb.model.TmdbTvDomainModel
import kotlinx.coroutines.flow.Flow

interface TmdbDomainRepository {
    suspend fun getDetailTvEpisode(
        seasonNumber: Int,
        episodeNumber: Int
    ): Flow<TmdbTvDomainModel>
}