package com.example.rickandmortyapp.domain.karakter.model.local

import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.karakter.model.network.KarakterModelItemModel

data class KarakterItemModelRoom(
    val id:Int?,
    val name:String?,
    val status:String?,
    val species:String?,
    val type:String?,
    val gender:String?,
    val image:String?,
    val created:String?
){
    companion object{
        fun transforms(models:List<KarakterItemModelRoom>): PagingData<KarakterModelItemModel>{
            return PagingData.from(models.map{
                transform(
                    KarakterItemModelRoom(
                        id = it.id,
                        name = it.name,
                        status = it.status,
                        species = it.species,
                        type = it.type,
                        gender = it.gender,
                        image = it.image,
                        created = it.created
                    )
                )
            })
        }

        private fun transform(model: KarakterItemModelRoom): KarakterModelItemModel {
            return KarakterModelItemModel(
                id = model.id,
                name = model.name,
                status = model.status,
                species = model.species,
                type = model.type,
                gender = model.gender,
                image = model.image,
                created = model.created,
                location = null,
                origin = null,
                episode = listOf()
            )
        }
    }
}
