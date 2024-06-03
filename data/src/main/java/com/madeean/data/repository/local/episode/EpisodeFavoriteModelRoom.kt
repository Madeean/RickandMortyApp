package com.madeean.data.repository.local.episode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.madeean.domain.episode.model.local.EpisodeItemFavoriteModelRoom

@Entity(tableName = "episode_favorite")
data class EpisodeFavoriteModelRoom(
    @PrimaryKey @ColumnInfo(name = "id_episode") val idEpisode:Int?,
){
    companion object{
        fun transforms(models: List<EpisodeFavoriteModelRoom>): List<EpisodeItemFavoriteModelRoom> {
            return models.map {
                transform(
                    EpisodeFavoriteModelRoom(
                        idEpisode = it.idEpisode,
                    )
                )
            }
        }

        private fun transform(model:EpisodeFavoriteModelRoom): EpisodeItemFavoriteModelRoom {
            return EpisodeItemFavoriteModelRoom(
                idEpisode = model.idEpisode,
            )
        }
    }
}
