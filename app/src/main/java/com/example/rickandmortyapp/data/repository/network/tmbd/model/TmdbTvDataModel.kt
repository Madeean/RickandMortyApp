package com.example.rickandmortyapp.data.repository.network.tmbd.model

import com.example.rickandmortyapp.domain.tmdb.model.TmdbTvDomainModel
import com.google.gson.annotations.SerializedName

data class TmdbTvDataModel(
    val id:Long,
    val overview:String,
    @SerializedName("still_path") val imagePoster:String
){
    companion object{
         fun transform(model:TmdbTvDataModel,error: String):TmdbTvDomainModel{
            return TmdbTvDomainModel(
                id = model.id,
                overview = model.overview,
                imagePoster = model.imagePoster,
                error = error
            )
        }
    }
}
