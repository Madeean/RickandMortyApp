package com.example.rickandmortyapp.data.repository.network.tmbd.model

import com.example.rickandmortyapp.domain.tmdb.model.TmdbTrailerDomainModel

data class TmdbTrailerDataModel(
    val key:String?,
){
    companion object{
        fun transforms(models:List<TmdbTrailerDataModel>, error:String):List<TmdbTrailerDomainModel>{
            return models.map { transform(it,error) }
        }

        private fun transform(model:TmdbTrailerDataModel, error:String):TmdbTrailerDomainModel{
            return TmdbTrailerDomainModel(
                key = model.key ?: "",
                error = error
            )
        }
    }
}
