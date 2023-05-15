package com.example.rickandmortyapp.data.repository.domain_repository

import com.example.rickandmortyapp.data.repository.network.tmbd.TmdbApiService
import com.example.rickandmortyapp.data.repository.network.tmbd.model.TmdbTvDataModel
import com.example.rickandmortyapp.domain.tmdb.TmdbDomainRepository
import com.example.rickandmortyapp.domain.tmdb.model.TmdbTvDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TmdbDomainRepositoryImpl @Inject constructor(
    private val tmdbApiService: TmdbApiService,
) : TmdbDomainRepository {
    override suspend fun getDetailTvEpisode(
        seasonNumber: Int, episodeNumber: Int
    ): Flow<TmdbTvDomainModel> {
        return flow {
            try {
                println("MASUK")
                val response = tmdbApiService.getDetailTvEpisode(
                    seasonNumber = seasonNumber, episodeNumber = episodeNumber
                )
                println("RESPONSE $response")
                emit(TmdbTvDataModel.transform(response, ""))
            } catch (e: java.lang.Exception) {
                println("ERROR ${e.message}")
                emit(TmdbTvDataModel.transform(TmdbTvDataModel(-1, "", ""), e.message ?: ""))
            }
        }.flowOn(Dispatchers.IO)
    }
}