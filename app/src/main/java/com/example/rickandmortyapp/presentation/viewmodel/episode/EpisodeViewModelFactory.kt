package com.example.rickandmortyapp.presentation.viewmodel.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmortyapp.domain.DomainUseCase
import javax.inject.Inject

class EpisodeViewModelFactory @Inject constructor(private var useCase: DomainUseCase): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(EpisodeViewModel::class.java) -> EpisodeViewModel(useCase) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}