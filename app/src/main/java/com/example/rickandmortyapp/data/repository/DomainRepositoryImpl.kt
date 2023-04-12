package com.example.rickandmortyapp.data.repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyapp.data.repository.di.LocalModule
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeFavoriteModelRoom
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeModelRoom
import com.example.rickandmortyapp.data.repository.local.karakter.KarakterFavoriteModelRoom
import com.example.rickandmortyapp.data.repository.local.karakter.KarakterModelRoom
import com.example.rickandmortyapp.data.repository.local.location.LocationFavoriteModelRoom
import com.example.rickandmortyapp.data.repository.local.location.LocationModelRoom
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeApiService
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeListPagingSource
import com.example.rickandmortyapp.data.repository.network.episode.MultipleEpisodePagingSource
import com.example.rickandmortyapp.data.repository.network.karakter.KarakterApiService
import com.example.rickandmortyapp.data.repository.network.karakter.KarakterListPagingSource
import com.example.rickandmortyapp.data.repository.network.karakter.MultipleKarakterPagingSource
import com.example.rickandmortyapp.data.repository.network.location.LocationApiService
import com.example.rickandmortyapp.data.repository.network.location.LocationListPagingSource
import com.example.rickandmortyapp.data.repository.network.location.MultipleLocationPagingSource
import com.example.rickandmortyapp.data.repository.network.location.model.LocationDetail
import com.example.rickandmortyapp.domain.DomainRepository
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DomainRepositoryImpl @Inject constructor(
    private val episodeApiService: EpisodeApiService,
    private val karakterApiService: KarakterApiService,
    private val locationApiService: LocationApiService
) : DomainRepository {

    override suspend fun getAllEpisode(
        scope: CoroutineScope, application: Application, name: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return Pager(config = PagingConfig(20)) {
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
        return Pager(config = PagingConfig(20)) {
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
        return Pager(config = PagingConfig(20)) {
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
        scope: CoroutineScope, application: Application, id: String
    ): Flow<PagingData<KarakterModelItemModel>> {
        return Pager(config = PagingConfig(1)) {
            MultipleKarakterPagingSource(
                application = application, apiService = karakterApiService, id = id
            )
        }.flow.cachedIn(scope)
    }

    override suspend fun getEpisodeById(
        scope: CoroutineScope, application: Application, id: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return Pager(config = PagingConfig(1)) {
            MultipleEpisodePagingSource(
                application = application, apiService = episodeApiService, id = id
            )
        }.flow.cachedIn(scope)
    }

    override suspend fun getLocationById(id: Int): Flow<LocationModelItemModel> {
        return flow {
            try {
                val response = locationApiService.getLocationById(id)
                emit(LocationDetail.transform(response))
            } catch (e: Exception) {
                emit(LocationModelItemModel(null, null, null, null, null, null))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMultipleLocationById(
        scope: CoroutineScope, id: String
    ): Flow<PagingData<LocationModelItemModel>> {
        return Pager(config = PagingConfig(1)) {
            MultipleLocationPagingSource(
                apiService = locationApiService, id = id
            )
        }.flow.cachedIn(scope)
    }

    override suspend fun getEpisodeRoom(application: Application): List<EpisodeItemModelRoom> {
        val episodeDao = LocalModule.getDatabase(application).episodeDao()
        val data = episodeDao.getAllEpisodeRoom()
        return EpisodeModelRoom.transfromsFroomRoomToDomain(data)
    }

    override suspend fun getKarakterRoom(application: Application): List<KarakterItemModelRoom> {
        val karakterDao = LocalModule.getDatabase(application).karakterDao()
        val data = karakterDao.getAllKarakterRoom()
        return KarakterModelRoom.transfromsFroomRoomToDomain(data)
    }

    override suspend fun getLocationRoom(application: Application): List<LocationItemModelRoom> {
        val locationDao = LocalModule.getDatabase(application).locationDao()
        val data = locationDao.getAllLocationRoom()
        return LocationModelRoom.transfromsFroomRoomToDomain(data)
    }

    override suspend fun insertFavoriteEpisode(application: Application, id: Int) {
        val episodeDao = LocalModule.getDatabase(application).episodeDao()
        episodeDao.insertEpisodeFavoriteRoom(EpisodeFavoriteModelRoom(id))

    }

    override suspend fun deleteFavoriteEpisode(application: Application, id: Int) {
        val episodeDao = LocalModule.getDatabase(application).episodeDao()
        episodeDao.deleteEpisodeFavoriteRoom(EpisodeFavoriteModelRoom(id))

    }

    override suspend fun getFavoriteEpisode(application: Application): List<EpisodeItemFavoriteModelRoom> {
        val episodeDao = LocalModule.getDatabase(application).episodeDao()
        val data = episodeDao.getAllEpisodeFavoriteRoom()
        return EpisodeFavoriteModelRoom.transforms(data)

    }

//    KARAKTER

    override suspend fun insertFavoriteKarakter(application: Application, id: Int) {
        val karakterDao = LocalModule.getDatabase(application).karakterDao()
        karakterDao.insertKarakterFavoriteRoom(KarakterFavoriteModelRoom(id))

    }

    override suspend fun deleteFavoriteKarakter(application: Application, id: Int) {
        val karakterDao = LocalModule.getDatabase(application).karakterDao()
        karakterDao.deleteKarakterFavoriteRoom(KarakterFavoriteModelRoom(id))

    }

    override suspend fun getFavoriteKarakter(application: Application): List<KarakterItemFavoriteModelRoom> {
        val karakterDao = LocalModule.getDatabase(application).karakterDao()
        val data = karakterDao.getAllKarakterFavoriteRoom()
        return KarakterFavoriteModelRoom.transforms(data)

    }

//    Location

    override suspend fun insertFavoriteLocation(application: Application, id: Int) {
        val locationDao = LocalModule.getDatabase(application).locationDao()
        locationDao.insertLocationFavoriteRoom(LocationFavoriteModelRoom(id))

    }

    override suspend fun deleteFavoriteLocation(application: Application, id: Int) {
        val locationDao = LocalModule.getDatabase(application).locationDao()
        locationDao.deleteLocationFavoriteRoom(LocationFavoriteModelRoom(id))

    }

    override suspend fun getFavoriteLocation(application: Application): List<LocationItemFavoriteModelRoom> {
        val locationDao = LocalModule.getDatabase(application).locationDao()
        val data = locationDao.getAllLocationFavoriteRoom()
        return LocationFavoriteModelRoom.transforms(data)

    }


}