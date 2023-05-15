package com.example.rickandmortyapp.domain.tmdb

import com.example.rickandmortyapp.domain.tmdb.model.TmdbTvDomainModel
import kotlinx.coroutines.flow.Flow

interface TmdbDomainUseCase {
    suspend fun getDetailTvEpisode(
        seasonNumber: Int,
        episodeNumber: Int
    ): Flow<TmdbTvDomainModel>
}