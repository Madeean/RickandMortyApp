package com.example.rickandmortyapp.data.repository.local.karakter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "karakter_favorite")
data class KarakterFavoriteModelRoom(
    @PrimaryKey @ColumnInfo(name = "id_karakter") val idKarakter:Int?,
)
