package com.example.rickandmortyapp.domain

import android.app.Application
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemModelRoom
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemModelRoom
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel
import com.example.rickandmortyapp.domain.model.location.local.LocationItemFavoriteModelRoom
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
        id: String
    ): Flow<PagingData<KarakterModelItemModel>> {
        return repository.getKarakterById(scope,id)
    }

    override suspend fun getEpisodeById(
        scope: CoroutineScope,
        id: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return repository.getEpisodeById(scope,id)
    }

    override suspend fun getLocationById(id: Int): Flow<LocationModelItemModel> {
        return repository.getLocationById(id)
    }

    override suspend fun getMultipleLocationById(scope: CoroutineScope,id: String): Flow<PagingData<LocationModelItemModel>> {
        return repository.getMultipleLocationById(scope,id)
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

    override suspend fun insertFavoriteEpisode(application: Application, id: Int) {
        return repository.insertFavoriteEpisode(application,id)
    }

    override suspend fun deleteFavoriteEpisode(application: Application, id: Int) {
        return repository.deleteFavoriteEpisode(application,id)
    }

    override suspend fun getFavoriteEpisode(application: Application): List<EpisodeItemFavoriteModelRoom> {
        return repository.getFavoriteEpisode(application)
    }

//    karakter

    override suspend fun insertFavoriteKarakter(application: Application, id: Int) {
        return repository.insertFavoriteKarakter(application,id)
    }

    override suspend fun deleteFavoriteKarakter(application: Application, id: Int) {
        return repository.deleteFavoriteKarakter(application,id)

    }

    override suspend fun getFavoriteKarakter(application: Application): List<KarakterItemFavoriteModelRoom> {
        return repository.getFavoriteKarakter(application)
    }

//    Location

    override suspend fun insertFavoriteLocation(application: Application, id: Int) {
        return repository.insertFavoriteLocation(application,id)
    }

    override suspend fun deleteFavoriteLocation(application: Application, id: Int) {
        return repository.deleteFavoriteLocation(application,id)

    }

    override suspend fun getFavoriteLocation(application: Application): List<LocationItemFavoriteModelRoom> {
        return repository.getFavoriteLocation(application)
    }


}