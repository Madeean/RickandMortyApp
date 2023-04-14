package com.example.rickandmortyapp.data.repository.domain_repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyapp.data.repository.di.LocalModule
import com.example.rickandmortyapp.data.repository.local.location.LocationFavoriteModelRoom
import com.example.rickandmortyapp.data.repository.local.location.LocationModelRoom
import com.example.rickandmortyapp.data.repository.network.location.LocationApiService
import com.example.rickandmortyapp.data.repository.network.location.LocationListPagingSource
import com.example.rickandmortyapp.data.repository.network.location.MultipleLocationPagingSource
import com.example.rickandmortyapp.data.repository.network.location.model.LocationDetail
import com.example.rickandmortyapp.domain.location.LocationDomainRepository
import com.example.rickandmortyapp.domain.location.model.local.LocationItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.location.model.local.LocationItemModelRoom
import com.example.rickandmortyapp.domain.location.model.network.LocationModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocationDomainRepositoryImpl @Inject constructor(
    private val locationApiService: LocationApiService,
) : LocationDomainRepository {

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

    override suspend fun getLocationRoom(application: Application): List<LocationItemModelRoom> {
        val locationDao = LocalModule.getDatabase(application).locationDao()
        val data = locationDao.getAllLocationRoom()
        return LocationModelRoom.transfromsFroomRoomToDomain(data)
    }

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