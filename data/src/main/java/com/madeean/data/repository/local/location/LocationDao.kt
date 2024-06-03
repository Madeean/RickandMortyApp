package com.madeean.data.repository.local.location

import androidx.room.*

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllLocationRoom(locationRoom: List<LocationModelRoom>)

    @Query("DELETE FROM location")
    fun deleteAllLocationRoom()

    @Query("SELECT * FROM location")
    fun getAllLocationRoom(): List<LocationModelRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocationFavoriteRoom(locationFavoriteRoom: LocationFavoriteModelRoom)

    @Delete
    fun deleteLocationFavoriteRoom(locationFavoriteRoom: LocationFavoriteModelRoom)

    @Query("SELECT * FROM location_favorite WHERE id_location > 0 ")
    fun getAllLocationFavoriteRoom(): List<LocationFavoriteModelRoom>

    @Query("DELETE FROM location_favorite")
    fun deleteAllLocationFavoriteRoom()
}