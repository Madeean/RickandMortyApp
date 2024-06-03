package com.madeean.domain.tmdb

import com.madeean.domain.tmdb.model.TmdbTrailerDomainModel
import com.madeean.domain.tmdb.model.TmdbTvDomainModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TmdbDomainUseCaseImpl @Inject constructor(private val repository: TmdbDomainRepository) :
    TmdbDomainUseCase {
    override suspend fun getDetailTvEpisode(
        seasonNumber: Int,
        episodeNumber: Int
    ): Flow<TmdbTvDomainModel> {
        return repository.getDetailTvEpisode(seasonNumber, episodeNumber)
    }

    override suspend fun getTrailerEpisode(
        seasonNumber: Int,
        episodeNumber: Int
    ): Flow<List<TmdbTrailerDomainModel>> {
        return repository.getTrailerEpisode(seasonNumber, episodeNumber)
    }
}