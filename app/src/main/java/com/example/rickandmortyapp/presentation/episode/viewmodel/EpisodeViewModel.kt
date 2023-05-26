package com.example.rickandmortyapp.presentation.episode.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.episode.EpisodeDomainUseCase
import com.example.rickandmortyapp.domain.episode.model.network.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.episode.model.local.EpisodeItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.episode.model.local.EpisodeItemModelRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EpisodeViewModel @Inject constructor(private val useCase: EpisodeDomainUseCase) :
    ViewModel() {
    private var _episode = MutableLiveData<PagingData<EpisodeModelItemModel>>()
    val episode: LiveData<PagingData<EpisodeModelItemModel>> = _episode

    suspend fun getAllEpisode(
        application: Application, name: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return useCase.getAllEpisode(viewModelScope, application, name)
    }

    suspend fun getEpisodeById(
        id: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return useCase.getEpisodeById(viewModelScope, id)
    }

    suspend fun getEpisodeRoom(
        application: Application
    ): List<EpisodeItemModelRoom> {
        return withContext(Dispatchers.IO) {
            useCase.getEpisodeRoom(application)
        }
    }

    suspend fun insertEpisodeFavoriteRoom(
        application: Application,
        id: Int,
    ) {
        return withContext(Dispatchers.IO) {
            useCase.insertFavoriteEpisode(application, id)
        }
    }

    suspend fun deleteEpisodeFavoriteRoom(
        application: Application,
        id: Int,
    ) {
        return withContext(Dispatchers.IO) {
            useCase.deleteFavoriteEpisode(application, id)
        }
    }

    suspend fun getEpisodeFavoriteRoom(
        application: Application
    ): List<EpisodeItemFavoriteModelRoom> {
        return withContext(Dispatchers.IO) {
            useCase.getFavoriteEpisode(application)
        }
    }
    val _itemCount = MutableLiveData<Int>()
    val itemCount: LiveData<Int> = _itemCount
    fun setItemAmount(itemCount: Int) {
        _itemCount.value = itemCount
    }
}