package com.example.rickandmortyapp.data.repository.local.episode

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmortyapp.data.repository.network.episode.model.EpisodeDetail
import com.example.rickandmortyapp.domain.model.episode.local.EpisodeItemModelRoom
import kotlinx.parcelize.Parcelize


@Entity(tableName = "episode")
@Parcelize
data class EpisodeModelRoom(
    @PrimaryKey @ColumnInfo(name = "id_episode") val idEpisode:Int?,
    @ColumnInfo(name = "name") val name:String?,
    @ColumnInfo(name = "air_date") val airDate:String?,
    @ColumnInfo(name = "episode") val episode:String?,
    @ColumnInfo(name = "created") val created:String?,
):Parcelable{
    companion object{
        fun transforms(models:List<EpisodeDetail>):List<EpisodeModelRoom>{
            return models.map {
                transform(
                    EpisodeDetail(
                        it.id,
                        it.name,
                        it.airDate,
                        it.episode,
                        it.created,
                        it.characterList
                    )
                )
            }
        }

        private fun transform(model:EpisodeDetail):EpisodeModelRoom{
            return EpisodeModelRoom(
                idEpisode = model.id,
                name = model.name,
                airDate = model.airDate,
                episode = model.episode,
                created = model.created,
            )
        }

        fun transfromsFroomRoomToDomain(models:List<EpisodeModelRoom>):List<EpisodeItemModelRoom>{
            return models.map {
                transformFromRoomToDomain(
                    EpisodeModelRoom(
                        it.idEpisode,
                        it.name,
                        it.airDate,
                        it.episode,
                        it.created,
                    )
                )
            }
        }

        private fun transformFromRoomToDomain(model:EpisodeModelRoom):EpisodeItemModelRoom{
            return EpisodeItemModelRoom(
                id = model.idEpisode,
                name = model.name,
                airDate = model.airDate,
                episode = model.episode,
                created = model.created,
            )
        }
    }
}
