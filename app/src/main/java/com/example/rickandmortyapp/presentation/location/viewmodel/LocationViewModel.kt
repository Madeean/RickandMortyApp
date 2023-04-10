package com.example.rickandmortyapp.presentation.location.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.DomainUseCase
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel
import com.example.rickandmortyapp.domain.model.location.local.LocationItemModelRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private var _locationById = MutableLiveData<LocationModelItemModel>()
    val locationById: LiveData<LocationModelItemModel> = _locationById

    fun getLocationById(id:Int){
        viewModelScope.launch {
            useCase.getLocationById(id).collect{
                _locationById.postValue(it)
            }
        }
    }

    suspend fun getLocationRoom(
        application: Application
    ):List<LocationItemModelRoom>{
        return withContext(Dispatchers.IO){
            useCase.getLocationRoom(application)
        }
    }
}