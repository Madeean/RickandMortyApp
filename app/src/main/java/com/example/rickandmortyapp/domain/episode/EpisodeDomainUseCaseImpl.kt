package com.example.rickandmortyapp.domain.episode

import android.app.Application
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.episode.model.local.EpisodeItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.episode.model.local.EpisodeItemModelRoom
import com.example.rickandmortyapp.domain.episode.model.network.EpisodeModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodeDomainUseCaseImpl @Inject constructor(private val repository: EpisodeDomainRepository) :
    EpisodeDomainUseCase {
    override suspend fun getAllEpisode(
        scope: CoroutineScope,
        application: Application,
        name:String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return repository.getAllEpisode(scope, application,name)
    }

    override suspend fun getEpisodeById(
        scope: CoroutineScope,
        id: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return repository.getEpisodeById(scope,id)
    }

    override suspend fun getEpisodeRoom(application: Application): List<EpisodeItemModelRoom> {
        return repository.getEpisodeRoom(application)
    }

    override suspend fun insertFavoriteEpisode(application: Application, id: Int) {
        return repository.insertFavoriteEpisode(application,id)
    }

    override suspend fun deleteFavoriteEpisode(application: Application, id: Int) {
        return repository.deleteFavoriteEpisode(application,id)
    }

    override suspend fun getFavoriteEpisode(application: Application): List<EpisodeItemFavoriteModelRoom> {
        return repository.getFavoriteEpisode(application)
    }
}