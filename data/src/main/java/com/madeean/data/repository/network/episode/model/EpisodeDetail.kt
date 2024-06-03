package com.madeean.data.repository.network.episode.model

import com.google.gson.annotations.SerializedName
import com.madeean.domain.episode.model.network.EpisodeModelItemModel

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

        private fun transform(model:EpisodeDetail?): EpisodeModelItemModel {
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
