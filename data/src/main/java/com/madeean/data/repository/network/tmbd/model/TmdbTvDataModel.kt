package com.madeean.data.repository.network.tmbd.model

import com.google.gson.annotations.SerializedName
import com.madeean.domain.tmdb.model.TmdbTvDomainModel

data class TmdbTvDataModel(
    val id:Long?,
    val overview:String?,
    @SerializedName("still_path") val imagePoster:String?
){
    companion object{
         fun transform(model:TmdbTvDataModel,error: String): TmdbTvDomainModel {
            return TmdbTvDomainModel(
                id = model.id ?: -1,
                overview = model.overview ?: "",
                imagePoster = model.imagePoster ?:"",
                error = error
            )
        }
    }
}
