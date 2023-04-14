package com.example.rickandmortyapp.data.repository.network.episode

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapp.data.repository.network.episode.model.EpisodeDetail
import com.example.rickandmortyapp.domain.episode.model.network.EpisodeModelItemModel

class MultipleEpisodePagingSource(
    private val apiService: EpisodeApiService,
    private val id: String
): PagingSource<Int, EpisodeModelItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, EpisodeModelItemModel>): Int? {
        return null
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeModelItemModel> {
        return try {

            val respone =
                apiService.getEpisodeById(id)
            val data = EpisodeDetail.transforms(respone)

             toLoadResult(
                data = data
            )

        } catch (e: java.lang.Exception) {
             LoadResult.Error(e)
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