package com.example.rickandmortyapp.presentation.viewmodel.karakter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.presentation.viewmodel.episode.EpisodeViewModel
import javax.inject.Inject

class KarakterViewModelFactory @Inject constructor(private var useCase: DomainUseCase): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(KarakterViewModel::class.java) -> KarakterViewModel(useCase) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}