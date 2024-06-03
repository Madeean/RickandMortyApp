package com.example.rickandmortyapp.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import com.example.rickandmortyapp.presentation.tmdb.TmdbViewModel
import com.madeean.domain.episode.EpisodeDomainUseCase
import com.madeean.domain.karakter.KarakterDomainUseCase
import com.madeean.domain.location.LocationDomainUseCase
import com.madeean.domain.tmdb.TmdbDomainUseCase
import javax.inject.Inject

class PresentationFactory @Inject constructor(
    private var episodeUseCase: EpisodeDomainUseCase,
    private var karakterUseCase: KarakterDomainUseCase,
    private var locationUseCase: LocationDomainUseCase,
    private var tmdbUseCase: TmdbDomainUseCase
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(KarakterViewModel::class.java) -> KarakterViewModel(
                karakterUseCase
            ) as T
            modelClass.isAssignableFrom(EpisodeViewModel::class.java) -> EpisodeViewModel(
                episodeUseCase
            ) as T
            modelClass.isAssignableFrom(LocationViewModel::class.java) -> LocationViewModel(
                locationUseCase
            ) as T
            modelClass.isAssignableFrom(TmdbViewModel::class.java) -> TmdbViewModel(
                tmdbUseCase
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}