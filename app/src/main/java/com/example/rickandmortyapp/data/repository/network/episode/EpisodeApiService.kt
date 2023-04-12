package com.example.rickandmortyapp.data.repository.network.episode

import com.example.rickandmortyapp.data.repository.network.episode.model.EpisodeDetail
import com.example.rickandmortyapp.data.repository.network.episode.model.EpisodeRespone
import com.example.rickandmortyapp.data.repository.network.karakter.model.KarakterDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodeApiService {
    @GET("episode/")
    suspend fun getEpisodeList(
        @Query("name") name: String,
        @Query("page") page: Int
    ): EpisodeRespone

    @GET("episode/{id}/")
    suspend fun getEpisodeById(
        @Path("id") id: String,
    ):List<EpisodeDetail>

}