package com.example.rickandmortyapp.domain.karakter

import android.app.Application
import androidx.paging.PagingData
import com.example.rickandmortyapp.domain.karakter.model.local.KarakterItemFavoriteModelRoom
import com.example.rickandmortyapp.domain.karakter.model.local.KarakterItemModelRoom
import com.example.rickandmortyapp.domain.karakter.model.network.KarakterModelItemModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface KarakterDomainRepository {
    suspend fun getAllKarakter(
        scope: CoroutineScope,
        application: Application,
        name:String,
        status:String,
        species:String,
        type:String,
        gender:String
    ): Flow<PagingData<KarakterModelItemModel>>

    suspend fun getKarakterById(
        scope:CoroutineScope,
        id:String
    ):Flow<PagingData<KarakterModelItemModel>>

    suspend fun getKarakterRoom(
        application: Application
    ):List<KarakterItemModelRoom>

    suspend fun insertFavoriteKarakter(
        application:Application,
        id:Int,
    )

    suspend fun deleteFavoriteKarakter(
        application:Application,
        id:Int,
    )

    suspend fun getFavoriteKarakter(
        application: Application
    ):List<KarakterItemFavoriteModelRoom>

}