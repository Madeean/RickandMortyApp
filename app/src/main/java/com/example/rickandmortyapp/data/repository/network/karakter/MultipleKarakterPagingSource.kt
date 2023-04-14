package com.example.rickandmortyapp.data.repository.network.karakter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapp.data.repository.network.karakter.model.KarakterDetail
import com.example.rickandmortyapp.domain.karakter.model.network.KarakterModelItemModel

class MultipleKarakterPagingSource(
    private val apiService: KarakterApiService,
    private val id: String
) : PagingSource<Int, KarakterModelItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, KarakterModelItemModel>): Int? {
        return null
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, KarakterModelItemModel> {
        return try {
            val response = apiService.getKarakterById(
                id = id
            )
            val data = KarakterDetail.transforms(response)

            toLoadResult(
                data = data
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun toLoadResult(
        data: List<KarakterModelItemModel>, prevKey: Int? = null, nextKey: Int? = null
    ): LoadResult<Int, KarakterModelItemModel> {
        return LoadResult.Page(
            data = data, prevKey = prevKey, nextKey = nextKey
        )
    }
}