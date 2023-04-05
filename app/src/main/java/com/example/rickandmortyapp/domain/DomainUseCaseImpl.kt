package com.example.rickandmortyapp.domain

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelEntity
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DomainUseCaseImpl @Inject constructor(private val repository: DomainRepository) :DomainUseCase {
    override  fun getAllEpisode(
        scope: CoroutineScope,
        application: Application,
        name:String
    ): LiveData<PagingData<EpisodeModelItemModel>> {
        return repository.getAllEpisode(scope, application,name)
    }
}