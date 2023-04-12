package com.example.rickandmortyapp.data.repository.network.location

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapp.data.repository.network.location.model.LocationDetail
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel

class MultipleLocationPagingSource(
    private val apiService: LocationApiService, private val id: String
) : PagingSource<Int, LocationModelItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, LocationModelItemModel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationModelItemModel> {
        return try {
            val response = apiService.getMultipleLocationById(
                id = id
            )
            println("RESPONSE ${response[0].id}, ${response[0].name}")
            val responseBersih = response.filter {
                (it.id ?: -1) > 0
            }
            val data = LocationDetail.transforms(responseBersih)
            toLoadResult(
                data = data
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }


    }

    private fun toLoadResult(
        data: List<LocationModelItemModel>, prevKey: Int? = null, nextKey: Int? = null
    ): PagingSource.LoadResult<Int, LocationModelItemModel> {
        return PagingSource.LoadResult.Page(
            data = data, prevKey = prevKey, nextKey = nextKey
        )
    }


}