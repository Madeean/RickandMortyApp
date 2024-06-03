package com.madeean.data.repository.network.episode

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.madeean.data.repository.di.LocalModule
import com.madeean.data.repository.local.episode.EpisodeDao
import com.madeean.data.repository.local.episode.EpisodeModelRoom
import com.madeean.data.repository.network.episode.model.EpisodeDetail
import com.madeean.domain.episode.model.network.EpisodeModelItemModel
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

            val respone = apiService.getEpisodeList(name, page = position)
            val data = EpisodeDetail.transforms(respone.results)
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
        executorService: ExecutorService, episodeDao: EpisodeDao, results: List<EpisodeDetail>
    ) {
        val dataSudahTransform = EpisodeModelRoom.transforms(results)
        executorService.execute {
            episodeDao.insertAllEpisodeRoom(dataSudahTransform)
        }
    }

    private fun deleteAllDbRoom(executorService: ExecutorService, episodeDao: EpisodeDao) {
        executorService.execute {
            episodeDao.deleteAllEpisodeRoom()
        }

    }

    private fun toLoadResult(
        data: List<EpisodeModelItemModel>, prevKey: Int? = null, nextKey: Int? = null
    ): LoadResult<Int, EpisodeModelItemModel> {
        return LoadResult.Page(
            data = data, prevKey = prevKey, nextKey = nextKey
        )
    }

}