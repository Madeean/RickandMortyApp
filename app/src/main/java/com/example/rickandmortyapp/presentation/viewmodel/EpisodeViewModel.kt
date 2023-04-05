package com.example.rickandmortyapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelEntity
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodeViewModel @Inject constructor(private val useCase: DomainUseCase):ViewModel() {
    private var _episode = MutableLiveData<PagingData<EpisodeModelItemModel>>()
    val episode: LiveData<PagingData<EpisodeModelItemModel>> = _episode

    suspend fun getAllEpisode(application: Application,name:String): Flow<PagingData<EpisodeModelItemModel>> {
        return useCase.getAllEpisode(viewModelScope, application,name)
    }
}