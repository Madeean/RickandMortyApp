package com.example.rickandmortyapp.domain

import android.app.Application
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelEntity
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
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
}