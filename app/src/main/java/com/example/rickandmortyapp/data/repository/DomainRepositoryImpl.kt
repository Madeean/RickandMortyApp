package com.example.rickandmortyapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeApiService
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeListPagingSource
import com.example.rickandmortyapp.domain.DomainRepository
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelEntity
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DomainRepositoryImpl @Inject constructor(
    private val apiService: EpisodeApiService
) : DomainRepository {

    override fun getAllEpisode(
        scope: CoroutineScope, application: Application, name: String
    ): LiveData<PagingData<EpisodeModelItemModel>> {
        println("masuk 3")
        return Pager(config = PagingConfig(1)) {
            EpisodeListPagingSource(apiService, application, name)
        }.liveData
    }

}