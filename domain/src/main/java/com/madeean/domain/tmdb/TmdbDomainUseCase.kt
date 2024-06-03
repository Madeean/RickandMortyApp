package com.madeean.domain.tmdb

import com.madeean.domain.tmdb.model.TmdbTrailerDomainModel
import com.madeean.domain.tmdb.model.TmdbTvDomainModel
import kotlinx.coroutines.flow.Flow

interface TmdbDomainUseCase {
    suspend fun getDetailTvEpisode(
        seasonNumber: Int,
        episodeNumber: Int
    ): Flow<TmdbTvDomainModel>

    suspend fun getTrailerEpisode(
        seasonNumber: Int,
        episodeNumber: Int
    ): Flow<List<TmdbTrailerDomainModel>>
}