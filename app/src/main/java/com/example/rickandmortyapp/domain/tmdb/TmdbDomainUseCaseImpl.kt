package com.example.rickandmortyapp.domain.tmdb

import com.example.rickandmortyapp.domain.tmdb.model.TmdbTvDomainModel
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
}