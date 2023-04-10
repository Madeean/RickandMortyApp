package com.example.rickandmortyapp.domain.model.location.local

import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.model.karakter.KarakterModelItemModel
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemModelRoom
import com.example.rickandmortyapp.domain.model.location.LocationModelItemModel

data class LocationItemModelRoom(
    val id: Int?,
    val name: String?,
    val type: String?,
    val dimension: String?,
    val created: String?
){
    companion object{
        fun transforms(models:List<LocationItemModelRoom>): PagingData<LocationModelItemModel> {
            return PagingData.from(models.map{
                transform(
                    LocationItemModelRoom(
                        id = it.id,
                        name = it.name,
                        type = it.type,
                        dimension = it.dimension,
                        created = it.created
                    )
                )
            })
        }

        private fun transform(model: LocationItemModelRoom): LocationModelItemModel {
            return LocationModelItemModel(
                id = model.id,
                name = model.name,
                type = model.type,
                dimension = model.dimension,
                created = model.created,
                residents = listOf()
            )
        }
    }
}