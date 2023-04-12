package com.example.rickandmortyapp.data.repository.network.karakter

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapp.data.repository.di.LocalModule
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeDao
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeModelRoom
import com.example.rickandmortyapp.data.repository.local.karakter.KarakterDao
import com.example.rickandmortyapp.data.repository.local.karakter.KarakterModelRoom
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeApiService
import com.example.rickandmortyapp.data.repository.network.karakter.model.KarakterDetail
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class KarakterListPagingSource(
    private val apiService: KarakterApiService,
    private val application: Application,
    private val name: String,
    private val status: String,
    private val species: String,
    private val type: String,
    private val gender: String
) : PagingSource<Int, KarakterModelItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, KarakterModelItemModel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, KarakterModelItemModel> {
        val position = params.key ?: 1
        return try {
            val respone = apiService.getAllKarakter(
                name = name,
                page = position,
                status = status,
                species = species,
                gender = gender,
                type = type
            )
            val data = KarakterDetail.transforms(respone.results)
            if(position == 1) respone.results?.let { saveToRoom(it) }

            toLoadResult(
                data = data, nextKey = if (data.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun saveToRoom(results: List<KarakterDetail>) {
        val karakterDao: KarakterDao
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        val db = LocalModule.getDatabase(application)
        karakterDao = db.karakterDao()
        deleteAllDbRoom(executorService, karakterDao)
        insetAllDataRoom(executorService, karakterDao, results)
    }

    private fun insetAllDataRoom(executorService: ExecutorService, karakterDao: KarakterDao, results: List<KarakterDetail>) {
        val dataSudahTransform = KarakterModelRoom.transforms(results)
        executorService.execute {
            karakterDao.insertAllKarakterRoom(dataSudahTransform)
        }

    }

    private fun deleteAllDbRoom(executorService: ExecutorService, karakterDao: KarakterDao) {
        executorService.execute {
            karakterDao.deleteAllKarakterRoom()
        }
    }

    private fun toLoadResult(
        data: List<KarakterModelItemModel>, prevKey: Int? = null, nextKey: Int? = null
    ): PagingSource.LoadResult<Int, KarakterModelItemModel> {
        return PagingSource.LoadResult.Page(
            data = data, prevKey = prevKey, nextKey = nextKey
        )
    }
}