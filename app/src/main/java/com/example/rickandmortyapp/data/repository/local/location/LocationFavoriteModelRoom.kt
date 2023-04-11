package com.example.rickandmortyapp.data.repository.local.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_favorite")
data class LocationFavoriteModelRoom(
    @PrimaryKey @ColumnInfo(name = "id_location") val idLocation:Int?,
)
