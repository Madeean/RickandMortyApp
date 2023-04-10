package com.example.rickandmortyapp.domain

import android.app.Application
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemModelRoom
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemModelRoom
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel
import com.example.rickandmortyapp.domain.model.location.local.LocationItemModelRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface DomainUseCase {

    suspend fun getAllEpisode(
        scope:CoroutineScope,
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
        application: Application,
        id:String
    ):Flow<PagingData<KarakterModelItemModel>>
    suspend fun getEpisodeById(
        scope:CoroutineScope,
        application: Application,
        id:String
    ):Flow<PagingData<EpisodeModelItemModel>>

    suspend fun getLocationById(
        id:Int
    ):Flow<LocationModelItemModel>


    suspend fun getEpisodeRoom(
        application: Application
    ):List<EpisodeItemModelRoom>
    suspend fun getKarakterRoom(
        application: Application
    ):List<KarakterItemModelRoom>

    suspend fun getLocationRoom(
        application: Application
    ):List<LocationItemModelRoom>

}