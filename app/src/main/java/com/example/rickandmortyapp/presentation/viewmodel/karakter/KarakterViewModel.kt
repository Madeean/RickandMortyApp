package com.example.rickandmortyapp.presentation.viewmodel.karakter

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KarakterViewModel @Inject constructor(private val useCase: DomainUseCase) : ViewModel() {

    private var _karakter = MutableLiveData<PagingData<KarakterModelItemModel>>()
    val karakter: LiveData<PagingData<KarakterModelItemModel>> = _karakter

    suspend fun getAllKarakter(
        application: Application,
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    ): Flow<PagingData<KarakterModelItemModel>> {
        println("MASUK 2")
        return useCase.getAllKarakter(viewModelScope, application, name,status,species,type,gender)
    }

    suspend fun getKarakterById(
        application: Application,
        id: String
    ): Flow<PagingData<KarakterModelItemModel>> {
        return useCase.getKarakterById(viewModelScope, application, id)
    }
}