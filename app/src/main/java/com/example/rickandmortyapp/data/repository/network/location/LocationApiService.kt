package com.example.rickandmortyapp.data.repository.network.location

import com.example.rickandmortyapp.data.repository.network.location.model.LocationDetail
import com.example.rickandmortyapp.data.repository.network.location.model.LocationRespone
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationApiService {

    @GET("location/")
    suspend fun getAllLocation(
        @Query("name") name:String,
        @Query("page") page: Int,
        @Query("type") type: String,
        @Query("dimension") dimension: String
    ):LocationRespone

    @GET("location/{id}")
    suspend fun getLocationById(
        @Path("id") id:Int
    ):LocationDetail
}