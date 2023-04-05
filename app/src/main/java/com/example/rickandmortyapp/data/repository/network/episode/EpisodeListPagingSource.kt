package com.example.rickandmortyapp.data.repository.network.episode

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapp.data.repository.network.episode.model.EpisodeDetail
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelEntity
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel

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
            println("masuk 2")
            val respone =
                apiService.getEpisodeList(name, page = if (position == 1) 1 else position * 10 - 10)
            val data = EpisodeDetail.transforms(respone.results)

            println(data)

            toLoadResult(
                data = data, nextKey = if (data.isEmpty()) null else position + 1
            )

        } catch (e: java.lang.Exception) {
            LoadResult.Error(e)
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