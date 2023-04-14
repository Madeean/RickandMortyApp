package com.example.rickandmortyapp.domain.episode

import android.app.Application
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.episode.model.local.EpisodeItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.episode.model.local.EpisodeItemModelRoom
import com.example.rickandmortyapp.domain.episode.model.network.EpisodeModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface EpisodeDomainRepository {
    suspend fun getAllEpisode(
        scope: CoroutineScope,
        application: Application,
        name:String
    ): Flow<PagingData<EpisodeModelItemModel>>

    suspend fun getEpisodeById(
        scope:CoroutineScope,
        id:String
    ):Flow<PagingData<EpisodeModelItemModel>>

    suspend fun getEpisodeRoom(
        application: Application
    ):List<EpisodeItemModelRoom>

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
}