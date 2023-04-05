package com.example.rickandmortyapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelEntity
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodeViewModel @Inject constructor(private val useCase: DomainUseCase) : ViewModel() {

    private val _quote = MutableLiveData<PagingData<EpisodeModelItemModel>>()
    val quote : LiveData<PagingData<EpisodeModelItemModel>> = _quote

    fun getAllQuote(name:String) {
        useCase.getAllEpisode(viewModelScope, Application(),name).cachedIn(viewModelScope).observeForever {
            _quote.value = it
        }
    }


}