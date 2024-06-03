package com.madeean.domain.episode.model.local

import androidx.paging.PagingData
import com.madeean.domain.episode.model.network.EpisodeModelItemModel

data class EpisodeItemModelRoom(
    val id: Int?,
    val name: String?,
    val airDate: String?,
    val episode: String?,
    val created: String?,
) {
    companion object {
        fun transformFromDomainToRoom(models: List<EpisodeItemModelRoom>): PagingData<EpisodeModelItemModel> {
            return PagingData.from(models.map {
                tranform(
                    EpisodeItemModelRoom(
                        it.id,
                        it.name,
                        it.airDate,
                        it.episode,
                        it.created,
                    )
                )
            })
        }

        private fun tranform(model: EpisodeItemModelRoom): EpisodeModelItemModel {
            return EpisodeModelItemModel(
                id = model.id,
                name = model.name,
                airDate = model.airDate,
                episode = model.episode,
                created = model.created,
                characterList = listOf()
            )
        }
    }
}
