package com.example.rickandmortyapp.presentation.viewmodel.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.presentation.viewmodel.karakter.KarakterViewModel
import javax.inject.Inject

class LocationViewModelFactory @Inject constructor(private var useCase: DomainUseCase): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LocationViewModel::class.java) -> LocationViewModel(useCase) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}