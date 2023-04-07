package com.example.rickandmortyapp.data.repository.network.location

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapp.data.repository.network.karakter.KarakterApiService
import com.example.rickandmortyapp.data.repository.network.location.model.LocationDetail
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel

class LocationListPagingSource(
    private val apiService: LocationApiService,
    private val application: Application,
    private val name: String,
    private val type: String,
    private val dimension: String,
): PagingSource<Int, LocationModelItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, LocationModelItemModel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationModelItemModel> {
        val position = params.key ?: 1
        return try{
            val respone = apiService.getAllLocation(
                name = name,
                page = if (position == 1) 1 else position * 10 - 10,
                type = type,
                dimension = dimension
            )

            val data = LocationDetail.transforms(respone.results)
            toLoadResult(
                data = data, nextKey = if (data.isEmpty()) null else position + 1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
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