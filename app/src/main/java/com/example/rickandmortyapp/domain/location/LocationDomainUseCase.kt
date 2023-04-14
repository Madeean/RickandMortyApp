package com.example.rickandmortyapp.domain.location

import android.app.Application
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.location.model.local.LocationItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.location.model.local.LocationItemModelRoom
import com.example.rickandmortyapp.domain.location.model.network.LocationModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface LocationDomainUseCase {

    suspend fun getAllLocation(
        scope: CoroutineScope,
        application: Application,
        name:String,
        type:String,
        dimension:String
    ): Flow<PagingData<LocationModelItemModel>>

    suspend fun getLocationById(
        id:Int
    ): Flow<LocationModelItemModel>

    suspend fun getMultipleLocationById(
        scope: CoroutineScope,
        id:String
    ): Flow<PagingData<LocationModelItemModel>>

    suspend fun getLocationRoom(
        application: Application
    ):List<LocationItemModelRoom>

    suspend fun insertFavoriteLocation(
        application: Application,
        id:Int,
    )

    suspend fun deleteFavoriteLocation(
        application: Application,
        id:Int,
    )

    suspend fun getFavoriteLocation(
        application: Application
    ):List<LocationItemFavoriteModelRoom>
}