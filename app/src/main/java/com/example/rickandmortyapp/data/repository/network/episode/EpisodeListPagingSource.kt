package com.example.rickandmortyapp.data.repository.network.episode

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapp.data.repository.di.LocalModule
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeDao
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeModelRoom
import com.example.rickandmortyapp.data.repository.network.episode.model.EpisodeDetail
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelEntity
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EpisodeListPagingSource(
    private val apiService: EpisodeApiService,
    private val application: Application,
    private val name: String
) : PagingSource<Int, EpisodeModelItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, EpisodeModelItemModel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeModelItemModel> {
        val position = params.key ?: 1
        return try {

//            deleteAllDbRoom(executorService, episodeDao)
            val respone =
                apiService.getEpisodeList(name, page = if (position == 1) 1 else position * 10 - 10)
            val data = EpisodeDetail.transforms(respone.results)
//            saveToRoom(executorService, episodeDao, respone.results)
            if (position == 1) {
                saveToRoom(respone.results)
            }


            toLoadResult(
                data = data, nextKey = if (data.isEmpty()) null else position + 1
            )

        } catch (e: java.lang.Exception) {
            LoadResult.Error(e)
        }
    }

    private fun saveToRoom(results: List<EpisodeDetail>) {
        val episodeDao: EpisodeDao
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        val db = LocalModule.getDatabase(application)
        episodeDao = db.episodeDao()

        deleteAllDbRoom(executorService, episodeDao)
        insetAllDataRoom(executorService, episodeDao, results)
    }

    private fun insetAllDataRoom(
        executorService: ExecutorService,
        episodeDao: EpisodeDao,
        results: List<EpisodeDetail>
    ) {
        val dataSudahTransform = EpisodeModelRoom.transforms(results)
        println("SUDAH MASUK")
        executorService.execute {
            episodeDao.insertAllEpisodeRoom(dataSudahTransform)
        }
    }

//    private fun saveToRoom(
//        executorService: ExecutorService, episodeDao: EpisodeDao, data: List<EpisodeDetail>
//    ) {
//        val episodeDao: EpisodeDao
//        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
//        val db = LocalModule.getDatabase(application)
//        episodeDao = db.episodeDao()
//
//        val dataSudahTransform = EpisodeModelRoom.transforms(data)
//        println("SUDAH MASUK")
//        executorService.execute {
//            episodeDao.insertAllEpisodeRoom(dataSudahTransform)
//        }
//    }

    private fun deleteAllDbRoom(executorService: ExecutorService, episodeDao: EpisodeDao) {
        executorService.execute {
            episodeDao.deleteAllEpisodeRoom()
        }

    }

    private fun toLoadResult(
        data: List<EpisodeModelItemModel>, prevKey: Int? = null, nextKey: Int? = null
    ): PagingSource.LoadResult<Int, EpisodeModelItemModel> {
        return PagingSource.LoadResult.Page(
            data = data, prevKey = prevKey, nextKey = nextKey
        )
    }

}