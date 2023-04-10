package com.example.rickandmortyapp.data.repository.local.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllLocationRoom(locationRoom: List<LocationModelRoom>)

    @Query("DELETE FROM location")
    fun deleteAllLocationRoom()

    @Query("SELECT * FROM location")
    fun getAllLocationRoom(): List<LocationModelRoom>
}