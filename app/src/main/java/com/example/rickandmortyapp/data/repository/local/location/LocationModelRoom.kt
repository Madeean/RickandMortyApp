package com.example.rickandmortyapp.data.repository.local.location

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmortyapp.data.repository.network.location.model.LocationDetail
import com.example.rickandmortyapp.domain.model.location.local.LocationItemModelRoom
import kotlinx.parcelize.Parcelize

@Entity(tableName = "location")
@Parcelize
data class LocationModelRoom(
    @PrimaryKey @ColumnInfo(name = "id_location") val idLocation: Int?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "dimension") val dimension: String?,
    @ColumnInfo(name = "created") val created: String?
) : Parcelable{
    companion object{
        fun transforms(models:List<LocationDetail>):List<LocationModelRoom>{
            return models.map{
                transform(
                    LocationDetail(
                        id = it.id,
                        name = it.name,
                        type = it.type,
                        dimension = it.dimension,
                        created = it.created,
                        residents = null,
                    )
                )
            }
        }

        private fun transform(model: LocationDetail): LocationModelRoom {
            return LocationModelRoom(
                idLocation = model.id,
                name = model.name,
                type = model.type,
                dimension = model.dimension,
                created = model.created
            )
        }

        fun transfromsFroomRoomToDomain(models:List<LocationModelRoom>):List<LocationItemModelRoom>{
            return models.map {
                transformFromRoomToDomain(
                    LocationModelRoom(
                        idLocation = it.idLocation,
                        name = it.name,
                        type = it.type,
                        dimension = it.dimension,
                        created = it.created
                    )
                )
            }
        }

        private fun transformFromRoomToDomain(model: LocationModelRoom): LocationItemModelRoom {
            return LocationItemModelRoom(
                id = model.idLocation,
                name = model.name,
                type = model.type,
                dimension = model.dimension,
                created = model.created
            )
        }

    }
}
