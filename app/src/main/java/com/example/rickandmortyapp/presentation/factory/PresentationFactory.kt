package com.example.rickandmortyapp.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.presentation.episode.viewmodel.EpisodeViewModel
import com.example.rickandmortyapp.presentation.karakter.viewmodel.KarakterViewModel
import com.example.rickandmortyapp.presentation.location.viewmodel.LocationViewModel
import javax.inject.Inject

class PresentationFactory @Inject constructor(private var useCase: DomainUseCase): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(KarakterViewModel::class.java) -> KarakterViewModel(useCase) as T
            modelClass.isAssignableFrom(EpisodeViewModel::class.java) -> EpisodeViewModel(useCase) as T
            modelClass.isAssignableFrom(LocationViewModel::class.java) -> LocationViewModel(useCase) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}