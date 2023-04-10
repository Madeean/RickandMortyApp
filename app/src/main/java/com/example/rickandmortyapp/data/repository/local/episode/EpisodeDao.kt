package com.example.rickandmortyapp.data.repository.local.episode

import androidx.room.*

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEpisodeRoom(episodeRoom: List<EpisodeModelRoom>)

    @Query("DELETE FROM episode")
    fun deleteAllEpisodeRoom()

    @Query("SELECT * FROM episode")
    fun getAllEpisodeRoom(): List<EpisodeModelRoom>
}