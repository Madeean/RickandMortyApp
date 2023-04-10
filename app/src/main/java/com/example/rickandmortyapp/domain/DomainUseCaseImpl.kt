package com.example.rickandmortyapp.domain

import android.app.Application
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelEntity
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemModelRoom
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemModelRoom
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel
import com.example.rickandmortyapp.domain.model.location.local.LocationItemModelRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DomainUseCaseImpl @Inject constructor(private val repository: DomainRepository) :DomainUseCase {
    override suspend fun getAllEpisode(
        scope: CoroutineScope,
        application: Application,
        name:String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return repository.getAllEpisode(scope, application,name)
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
        println("MASSUK 3")
        return repository.getAllKarakter(scope,application,name,status,species,type,gender)
    }

    override suspend fun getAllLocation(
        scope: CoroutineScope,
        application: Application,
        name: String,
        type: String,
        dimension: String
    ): Flow<PagingData<LocationModelItemModel>> {
        return repository.getAllLocation(scope,application,name,type,dimension)
    }

    override suspend fun getKarakterById(
        scope: CoroutineScope,
        application: Application,
        id: String
    ): Flow<PagingData<KarakterModelItemModel>> {
        return repository.getKarakterById(scope,application,id)
    }

    override suspend fun getEpisodeById(
        scope: CoroutineScope,
        application: Application,
        id: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return repository.getEpisodeById(scope,application,id)
    }

    override suspend fun getLocationById(id: Int): Flow<LocationModelItemModel> {
        return repository.getLocationById(id)
    }

    override suspend fun getEpisodeRoom(application: Application): List<EpisodeItemModelRoom> {
        return repository.getEpisodeRoom(application)
    }

    override suspend fun getKarakterRoom(application: Application): List<KarakterItemModelRoom> {
        return repository.getKarakterRoom(application)
    }

    override suspend fun getLocationRoom(application: Application): List<LocationItemModelRoom> {
        return repository.getLocationRoom(application)
    }


}