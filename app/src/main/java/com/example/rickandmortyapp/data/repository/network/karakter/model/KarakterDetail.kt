package com.example.rickandmortyapp.data.repository.network.karakter.model

import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelLocationItemModel
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelOriginItemModel

data class KarakterDetail (
    val id:Int?,
    val name:String?,
    val status:String?,
    val species:String?,
    val type:String?,
    val gender:String?,
    val origin:KarakterOrigin?,
    val location:KarakterLocation?,
    val image:String?,
    val episode:List<String>?,
    val created:String?
){
    companion object{
        fun transforms(models:List<KarakterDetail>?):List<KarakterModelItemModel>{
            if(models != null){
                return models.map{
                    transform(
                        KarakterDetail(
                            id = it.id,
                            name = it.name,
                            status = it.status,
                            species = it.species,
                            type = it.type,
                            gender = it.gender,
                            origin = it.origin,
                            location = it.location,
                            image = it.image,
                            episode = it.episode,
                            created = it.created
                        )
                    )
                }
            }else{
                return listOf()
            }
        }

        private fun transform(model:KarakterDetail):KarakterModelItemModel{
            return KarakterModelItemModel(
                id = model.id,
                name = model.name,
                status = model.status,
                species = model.species,
                type = model.type,
                gender = model.gender,
                origin = KarakterModelOriginItemModel(
                    name = model.origin?.name,
                    url = model.origin?.url
                ),
                location = KarakterModelLocationItemModel(
                    name = model.location?.name,
                    url = model.location?.url
                ),
                image = model.image,
                episode = model.episode,
                created = model.created
            )
        }
    }
}