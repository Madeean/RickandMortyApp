package com.example.rickandmortyapp.data.repository.local.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmortyapp.domain.model.location.local.LocationItemFavoriteModelRoom

@Entity(tableName = "location_favorite")
data class LocationFavoriteModelRoom(
    @PrimaryKey @ColumnInfo(name = "id_location") val idLocation:Int?,
){
    companion object{
        fun transforms(models:List<LocationFavoriteModelRoom>):List<LocationItemFavoriteModelRoom>{
            return models.map{
                transform(
                    LocationFavoriteModelRoom(
                        idLocation = it.idLocation
                    )
                )
            }
        }

        private fun transform(model:LocationFavoriteModelRoom):LocationItemFavoriteModelRoom{
            return LocationItemFavoriteModelRoom(
                idLocation = model.idLocation
            )
        }
    }
}
