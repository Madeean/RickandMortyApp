package com.example.rickandmortyapp.domain

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelEntity
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface DomainUseCase {

    fun getAllEpisode(
        scope:CoroutineScope,
        application: Application,
        name:String
    ): LiveData<PagingData<EpisodeModelItemModel>>
}