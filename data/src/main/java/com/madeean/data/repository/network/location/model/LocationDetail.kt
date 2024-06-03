package com.madeean.data.repository.network.location.model

import com.madeean.domain.location.model.network.LocationModelItemModel


data class LocationDetail(
    val id: Int?,
    val name: String?,
    val type: String?,
    val dimension: String?,
    val residents: List<String>?,
    val created: String?
) {
    companion object {
        fun transforms(models: List<LocationDetail>?): List<LocationModelItemModel> {
            if(models != null){
                return models.map {
                    transform(
                        LocationDetail(
                            id = it.id,
                            name = it.name,
                            type = it.type,
                            dimension = it.dimension,
                            residents = it.residents,
                            created = it.created
                        )
                    )
                }
            }else{
                return listOf()
            }
        }

        fun transform(model: LocationDetail): LocationModelItemModel {
            return LocationModelItemModel(
                id = model.id,
                name = model.name,
                type = model.type,
                dimension = model.dimension,
                residents = model.residents,
                created = model.created
            )
        }
    }
}
