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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisodeFavoriteRoom(episodeFavoriteRoom: EpisodeFavoriteModelRoom)

    @Delete
    fun deleteEpisodeFavoriteRoom(episodeFavoriteRoom: EpisodeFavoriteModelRoom)

    @Query("SELECT * FROM episode_favorite")
    fun getAllEpisodeFavoriteRoom(): List<EpisodeFavoriteModelRoom>
}