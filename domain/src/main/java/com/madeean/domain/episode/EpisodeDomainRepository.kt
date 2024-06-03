package com.madeean.domain.episode

import android.app.Application
import androidx.paging.PagingData
import com.madeean.domain.episode.model.local.EpisodeItemFavoriteModelRoom
import com.madeean.domain.episode.model.local.EpisodeItemModelRoom
import com.madeean.domain.episode.model.network.EpisodeModelItemModel
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