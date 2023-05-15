package com.example.rickandmortyapp.presentation.tmdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.domain.tmdb.TmdbDomainUseCase
import com.example.rickandmortyapp.domain.tmdb.model.TmdbTvDomainModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class TmdbViewModel @Inject constructor(private val useCase: TmdbDomainUseCase) : ViewModel() {
    private var _tmdbDetailTv = MutableLiveData<TmdbTvDomainModel>()
    val tmdbDetailTv: LiveData<TmdbTvDomainModel> = _tmdbDetailTv

    fun getDetailTv(
        seasonNumber: Int,
        episodeNumber: Int
    ) {
        viewModelScope.launch {
            useCase.getDetailTvEpisode(seasonNumber = seasonNumber, episodeNumber = episodeNumber).collect{
                _tmdbDetailTv.postValue(it)
            }
        }
    }
}