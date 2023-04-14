package com.example.rickandmortyapp.domain.location

import android.app.Application
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.location.model.local.LocationItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.location.model.local.LocationItemModelRoom
import com.example.rickandmortyapp.domain.location.model.network.LocationModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationDomainUseCaseImpl  @Inject constructor(private val repository: LocationDomainRepository) :
    LocationDomainUseCase {

    override suspend fun getAllLocation(
        scope: CoroutineScope,
        application: Application,
        name: String,
        type: String,
        dimension: String
    ): Flow<PagingData<LocationModelItemModel>> {
        return repository.getAllLocation(scope,application,name,type,dimension)
    }

    override suspend fun getLocationById(id: Int): Flow<LocationModelItemModel> {
        return repository.getLocationById(id)
    }

    override suspend fun getMultipleLocationById(scope: CoroutineScope, id: String): Flow<PagingData<LocationModelItemModel>> {
        return repository.getMultipleLocationById(scope,id)
    }

    override suspend fun getLocationRoom(application: Application): List<LocationItemModelRoom> {
        return repository.getLocationRoom(application)
    }

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