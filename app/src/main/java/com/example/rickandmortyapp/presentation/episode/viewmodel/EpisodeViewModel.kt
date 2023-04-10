package com.example.rickandmortyapp.presentation.episode.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeModelRoom
import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelEntity
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemModelRoom
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EpisodeViewModel @Inject constructor(private val useCase: DomainUseCase) : ViewModel() {
    private var _episode = MutableLiveData<PagingData<EpisodeModelItemModel>>()
    val episode: LiveData<PagingData<EpisodeModelItemModel>> = _episode

    suspend fun getAllEpisode(
        application: Application, name: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return useCase.getAllEpisode(viewModelScope, application, name)
    }

    suspend fun getEpisodeById(
        application: Application, id: String
    ): Flow<PagingData<EpisodeModelItemModel>> {
        return useCase.getEpisodeById(viewModelScope, application, id)
    }

    suspend fun getEpisodeRoom(
        application: Application
    ): List<EpisodeItemModelRoom> {
        return withContext(Dispatchers.IO) {
            useCase.getEpisodeRoom(application)
        }
    }
}