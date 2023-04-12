package com.example.rickandmortyapp.presentation.karakter.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemModelRoom
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemModelRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
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

    suspend fun getKarakterRoom(
        application: Application
    ): List<KarakterItemModelRoom> {
        return withContext(Dispatchers.IO) {
            useCase.getKarakterRoom(application)
        }
    }


    suspend fun insertKarakterFavoriteRoom(
        application: Application,
        id: Int,
    ) {
        return withContext(Dispatchers.IO) {
            useCase.insertFavoriteKarakter(application, id)
        }
    }

    suspend fun deleteKarakterFavoriteRoom(
        application: Application,
        id:Int,
    ){
        return withContext(Dispatchers.IO){
            useCase.deleteFavoriteKarakter(application,id)
        }
    }

    suspend fun getKarakterFavoriteRoom(
        application: Application
    ): List<KarakterItemFavoriteModelRoom> {
        return withContext(Dispatchers.IO) {
            useCase.getFavoriteKarakter(application)
        }
    }
}