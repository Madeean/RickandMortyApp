package com.example.rickandmortyapp.data.repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeApiService
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeListPagingSource
import com.example.rickandmortyapp.data.repository.network.karakter.KarakterApiService
import com.example.rickandmortyapp.data.repository.network.karakter.KarakterListPagingSource
import com.example.rickandmortyapp.domain.DomainRepository
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DomainRepositoryImpl @Inject constructor(
    private val episodeApiService: EpisodeApiService,
    private val karakterApiService:KarakterApiService
) : DomainRepository {

    override suspend fun getAllEpisode(
        scope: CoroutineScope, application: Application, name: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return Pager(config = PagingConfig(1)) {
            EpisodeListPagingSource(episodeApiService, application, name)
        }.flow.cachedIn(scope)
    }

    override suspend fun getAllKarakter(
        scope: CoroutineScope,
        application: Application,
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    ): Flow<PagingData<KarakterModelItemModel>> {
        println("MASUK 4")
        return Pager(config = PagingConfig(1)) {
            KarakterListPagingSource(
                apiService = karakterApiService,
                application = application,
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender
            )
        }.flow.cachedIn(scope)
    }

}