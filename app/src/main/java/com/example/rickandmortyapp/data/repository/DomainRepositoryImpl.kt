package com.example.rickandmortyapp.data.repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeApiService
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeListPagingSource
import com.example.rickandmortyapp.data.repository.network.episode.MultipleEpisodePagingSource
import com.example.rickandmortyapp.data.repository.network.karakter.KarakterApiService
import com.example.rickandmortyapp.data.repository.network.karakter.KarakterListPagingSource
import com.example.rickandmortyapp.data.repository.network.karakter.MultipleKarakterPagingSource
import com.example.rickandmortyapp.data.repository.network.location.LocationApiService
import com.example.rickandmortyapp.data.repository.network.location.LocationListPagingSource
import com.example.rickandmortyapp.domain.DomainRepository
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DomainRepositoryImpl @Inject constructor(
    private val episodeApiService: EpisodeApiService,
    private val karakterApiService:KarakterApiService,
    private val locationApiService: LocationApiService
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

    override suspend fun getAllLocation(
        scope: CoroutineScope,
        application: Application,
        name: String,
        type: String,
        dimension: String
    ): Flow<PagingData<LocationModelItemModel>> {
        return Pager(config = PagingConfig(1)){
            LocationListPagingSource(
                apiService = locationApiService,
                application = application,
                name = name,
                type = type,
                dimension = dimension
            )
        }.flow.cachedIn(scope)
    }

    override suspend fun getKarakterById(
        scope: CoroutineScope,
        application: Application,
        id: String
    ): Flow<PagingData<KarakterModelItemModel>> {
        return Pager(config = PagingConfig(1)){
            MultipleKarakterPagingSource(
                application = application,
                apiService = karakterApiService,
                id = id
            )
        }.flow.cachedIn(scope)
    }

    override suspend fun getEpisodeById(
        scope: CoroutineScope,
        application: Application,
        id: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return Pager(config = PagingConfig(1)){
            MultipleEpisodePagingSource(
                application = application,
                apiService = episodeApiService,
                id = id
            )
        }.flow.cachedIn(scope)
    }

}