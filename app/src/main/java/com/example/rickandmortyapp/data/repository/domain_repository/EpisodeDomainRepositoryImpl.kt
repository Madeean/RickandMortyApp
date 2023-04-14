package com.example.rickandmortyapp.data.repository.domain_repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyapp.data.repository.di.LocalModule
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeFavoriteModelRoom
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeModelRoom
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeApiService
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeListPagingSource
import com.example.rickandmortyapp.data.repository.network.episode.MultipleEpisodePagingSource
import com.example.rickandmortyapp.domain.episode.EpisodeDomainRepository
import com.example.rickandmortyapp.domain.episode.model.local.EpisodeItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.episode.model.local.EpisodeItemModelRoom
import com.example.rickandmortyapp.domain.episode.model.network.EpisodeModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodeDomainRepositoryImpl @Inject constructor(
    private val episodeApiService: EpisodeApiService,
) : EpisodeDomainRepository {

    override suspend fun getAllEpisode(
        scope: CoroutineScope, application: Application, name: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return Pager(config = PagingConfig(20)) {
            EpisodeListPagingSource(episodeApiService, application, name)
        }.flow.cachedIn(scope)
    }

    override suspend fun getEpisodeById(
        scope: CoroutineScope, id: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return Pager(config = PagingConfig(1)) {
            MultipleEpisodePagingSource(
                apiService = episodeApiService, id = id
            )
        }.flow.cachedIn(scope)
    }

    override suspend fun getEpisodeRoom(application: Application): List<EpisodeItemModelRoom> {
        val episodeDao = LocalModule.getDatabase(application).episodeDao()
        val data = episodeDao.getAllEpisodeRoom()
        return EpisodeModelRoom.transfromsFroomRoomToDomain(data)
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

}