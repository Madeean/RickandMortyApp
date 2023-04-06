package com.example.rickandmortyapp.data.repository.network.karakter

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapp.data.repository.network.episode.EpisodeApiService
import com.example.rickandmortyapp.data.repository.network.karakter.model.KarakterDetail
import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel

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
        println("MASUK 5")
        return try {
            println("MASUK 6")
            val respone = apiService.getAllKarakter(
                name = name,
                page = if (position == 1) 1 else position * 10 - 10,
                status = status,
                species = species,
                gender = gender,
                type = type
            )
            println(respone)
            val data = KarakterDetail.transforms(respone.results)

            toLoadResult(
                data = data, nextKey = if (data.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            println(e.message)
            LoadResult.Error(e)
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