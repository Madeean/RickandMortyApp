package com.example.rickandmortyapp.data.repository.network.episode

import com.example.rickandmortyapp.data.repository.network.episode.model.EpisodeRespone
import retrofit2.http.GET
import retrofit2.http.Query

interface EpisodeApiService {
    @GET("episode/")
    suspend fun getEpisodeList(
        @Query("name") name: String,
        @Query("page") page: Int
    ): EpisodeRespone
}