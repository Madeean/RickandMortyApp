package com.example.rickandmortyapp.data.repository.local.karakter

import androidx.room.*

@Dao
interface KarakterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllKarakterRoom(karakterRoom: List<KarakterModelRoom>)

    @Query("DELETE FROM karakter")
    fun deleteAllKarakterRoom()

    @Query("SELECT * FROM karakter")
    fun getAllKarakterRoom(): List<KarakterModelRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKarakterFavoriteRoom(karakterFavoriteRoom: KarakterFavoriteModelRoom)

    @Delete
    fun deleteKarakterFavoriteRoom(karakterFavoriteRoom: KarakterFavoriteModelRoom)

    @Query("SELECT * FROM karakter_favorite")
    fun getAllKarakterFavoriteRoom(): List<KarakterFavoriteModelRoom>
}