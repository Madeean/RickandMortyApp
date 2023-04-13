package com.example.rickandmortyapp.data.repository.network.episode.model

import com.example.rickandmortyapp.domain.model.episode.EpisodeModelItemModel
import com.google.gson.annotations.SerializedName

data class EpisodeDetail(
    val id:Int?,
    val name:String?,
    @SerializedName("air_date") val airDate:String?,
    val episode:String?,
    val created:String?,
    @SerializedName("characters") val characterList:List<String>?,
){
    companion object{
        fun transforms(models:List<EpisodeDetail>?):List<EpisodeModelItemModel>{
            if (models != null) {
                return models.map{
                    transform(
                        EpisodeDetail(
                            it.id,
                            it.name,
                            it.airDate,
                            it.episode,
                            it.created,
                            it.characterList
                        ),
                    )
                }
            }else{
                return listOf()
            }
        }

        private fun transform(model:EpisodeDetail?):EpisodeModelItemModel{
            return EpisodeModelItemModel(
                model?.id,
                model?.name,
                model?.airDate,
                model?.episode,
                model?.created,
                model?.characterList,
            )
        }
    }
}
