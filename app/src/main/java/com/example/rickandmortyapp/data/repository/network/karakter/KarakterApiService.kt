package com.example.rickandmortyapp.data.repository.network.karakter

import com.example.rickandmortyapp.data.repository.network.karakter.model.KarakterDetail
import com.example.rickandmortyapp.data.repository.network.karakter.model.KarakterRespone
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KarakterApiService {

    @GET("character/")
    suspend fun getAllKarakter(
        @Query("name") name: String,
        @Query("page") page: Int,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("type") type: String,
        @Query("gender") gender:String
    ):KarakterRespone

    @GET("character/{id}/")
    suspend fun getKarakterById(
        @Path("id") id: String,
    ):List<KarakterDetail>
}