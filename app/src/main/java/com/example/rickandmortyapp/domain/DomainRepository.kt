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

interface DomainRepository {
    suspend fun getAllEpisode(
        scope: CoroutineScope,
        application: Application,
        name:String
    ): Flow<PagingData<EpisodeModelItemModel>>

    suspend fun getAllKarakter(
        scope:CoroutineScope,
        application: Application,
        name:String,
        status:String,
        species:String,
        type:String,
        gender:String
    ): Flow<PagingData<KarakterModelItemModel>>

    suspend fun getAllLocation(
        scope:CoroutineScope,
        application: Application,
        name:String,
        type:String,
        dimension:String
    ): Flow<PagingData<LocationModelItemModel>>

    suspend fun getKarakterById(
        scope:CoroutineScope,
        id:String
    ):Flow<PagingData<KarakterModelItemModel>>

    suspend fun getEpisodeById(
        scope:CoroutineScope,
        id:String
    ):Flow<PagingData<EpisodeModelItemModel>>

    suspend fun getLocationById(
        id:Int
    ):Flow<LocationModelItemModel>

    suspend fun getMultipleLocationById(
        scope: CoroutineScope,
        id:String
    ):Flow<PagingData<LocationModelItemModel>>

    suspend fun getEpisodeRoom(
        application: Application
    ):List<EpisodeItemModelRoom>

    suspend fun getKarakterRoom(
        application: Application
    ):List<KarakterItemModelRoom>

    suspend fun getLocationRoom(
        application: Application
    ):List<LocationItemModelRoom>

//    Episode
    suspend fun insertFavoriteEpisode(
        application:Application,
        id:Int,
    )

    suspend fun deleteFavoriteEpisode(
        application:Application,
        id:Int,
    )

    suspend fun getFavoriteEpisode(
        application: Application
    ):List<EpisodeItemFavoriteModelRoom>


    //    Karakter

    suspend fun insertFavoriteKarakter(
        application:Application,
        id:Int,
    )

    suspend fun deleteFavoriteKarakter(
        application:Application,
        id:Int,
    )

    suspend fun getFavoriteKarakter(
        application: Application
    ):List<KarakterItemFavoriteModelRoom>


    //    Location

    suspend fun insertFavoriteLocation(
        application:Application,
        id:Int,
    )

    suspend fun deleteFavoriteLocation(
        application:Application,
        id:Int,
    )

    suspend fun getFavoriteLocation(
        application: Application
    ):List<LocationItemFavoriteModelRoom>
}