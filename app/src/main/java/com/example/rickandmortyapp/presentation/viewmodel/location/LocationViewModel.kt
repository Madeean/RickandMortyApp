package com.example.rickandmortyapp.presentation.viewmodel.location

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationViewModel @Inject constructor(private val useCase: DomainUseCase) : ViewModel(){
    private var _location = MutableLiveData<PagingData<LocationModelItemModel>>()
    val location: LiveData<PagingData<LocationModelItemModel>> = _location

    suspend fun getAllLocation(
        application: Application,
        name:String,
        type:String,
        dimension:String
    ): Flow<PagingData<LocationModelItemModel>> {
        return useCase.getAllLocation(
            scope = viewModelScope,
            application = application,
            name = name,
            type = type,
            dimension = dimension
        )
    }
}