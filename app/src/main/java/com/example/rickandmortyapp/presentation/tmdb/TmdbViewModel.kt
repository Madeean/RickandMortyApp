package com.example.rickandmortyapp.presentation.tmdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madeean.domain.tmdb.TmdbDomainUseCase
import com.madeean.domain.tmdb.model.TmdbTrailerDomainModel
import com.madeean.domain.tmdb.model.TmdbTvDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
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

    private var _tmdbTrailetTv = MutableLiveData<List<TmdbTrailerDomainModel>>()
    val tmdbTrailetTv: LiveData<List<TmdbTrailerDomainModel>> = _tmdbTrailetTv

    fun getTrailerTv(
        seasonNumber: Int,
        episodeNumber: Int
    ) {
        viewModelScope.launch {
            useCase.getTrailerEpisode(seasonNumber = seasonNumber, episodeNumber = episodeNumber).collect{
                _tmdbTrailetTv.postValue(it)
            }
        }
    }

}