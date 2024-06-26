package com.example.rickandmortyapp.data.repository.network.tmbd

import com.example.rickandmortyapp.data.DataUtils
import com.example.rickandmortyapp.data.repository.network.tmbd.model.TmdbTrailerResponseDataModel
import com.example.rickandmortyapp.data.repository.network.tmbd.model.TmdbTvDataModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("60625/season/{season_number}/episode/{episode_number}")
    suspend fun getDetailTvEpisode(
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("api_key") apiKey: String = DataUtils.API_KEY,
    ): TmdbTvDataModel

    @GET("60625/season/{season_number}/episode/{episode_number}/videos")
    suspend fun getTrailerEpisode(
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("api_key") apiKey: String = DataUtils.API_KEY,
    ):TmdbTrailerResponseDataModel


}