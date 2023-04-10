package com.example.rickandmortyapp.data.repository.local.karakter

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmortyapp.data.repository.local.episode.EpisodeModelRoom

@Dao
interface KarakterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllKarakterRoom(karakterRoom: List<KarakterModelRoom>)

    @Query("DELETE FROM karakter")
    fun deleteAllKarakterRoom()

    @Query("SELECT * FROM karakter")
    fun getAllKarakterRoom(): List<KarakterModelRoom>
}