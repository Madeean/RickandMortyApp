package com.example.rickandmortyapp.data.repository.local.karakter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rickandmortyapp.domain.model.karakter.local.KarakterItemFavoriteModelRoom

@Entity(tableName = "karakter_favorite")
data class KarakterFavoriteModelRoom(
    @PrimaryKey @ColumnInfo(name = "id_karakter") val idKarakter:Int?,
){
    companion object{
        fun transforms(models: List<KarakterFavoriteModelRoom>):List<KarakterItemFavoriteModelRoom>{
            return models.map{
                transform(
                    KarakterFavoriteModelRoom(
                        idKarakter = it.idKarakter
                    )
                )
            }
        }

        private fun transform(model:KarakterFavoriteModelRoom):KarakterItemFavoriteModelRoom{
            return KarakterItemFavoriteModelRoom(
                idKarakter = model.idKarakter
            )
        }
    }
}
